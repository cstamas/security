package org.sonatype.jsecurity.realms;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.util.StringUtils;
import org.jsecurity.authc.AuthenticationException;
import org.jsecurity.authc.AuthenticationInfo;
import org.jsecurity.authc.AuthenticationToken;
import org.jsecurity.authc.credential.Sha1CredentialsMatcher;
import org.jsecurity.authz.AuthorizationException;
import org.jsecurity.authz.AuthorizationInfo;
import org.jsecurity.authz.Permission;
import org.jsecurity.authz.SimpleAuthorizationInfo;
import org.jsecurity.authz.permission.WildcardPermission;
import org.jsecurity.cache.HashtableCache;
import org.jsecurity.realm.AuthorizingRealm;
import org.jsecurity.realm.Realm;
import org.jsecurity.subject.PrincipalCollection;
import org.sonatype.jsecurity.locators.users.PlexusRole;
import org.sonatype.jsecurity.locators.users.PlexusUser;
import org.sonatype.jsecurity.locators.users.PlexusUserManager;
import org.sonatype.jsecurity.model.CPrivilege;
import org.sonatype.jsecurity.model.CRole;
import org.sonatype.jsecurity.realms.privileges.PrivilegeDescriptor;
import org.sonatype.jsecurity.realms.tools.ConfigurationManager;
import org.sonatype.jsecurity.realms.tools.NoSuchPrivilegeException;
import org.sonatype.jsecurity.realms.tools.NoSuchRoleException;

@Component( role = Realm.class, hint = "XmlAuthorizingRealm" )
public class XmlAuthorizingRealm
    extends AuthorizingRealm
    implements Realm
{
    @Requirement( role = ConfigurationManager.class, hint = "resourceMerging" )
    private ConfigurationManager configuration;
    
    @Requirement(role=PlexusUserManager.class, hint="additinalRoles")
    private PlexusUserManager userManager;
    
    @Requirement(role=PrivilegeDescriptor.class)
    private List<PrivilegeDescriptor> privilegeDescriptors;

    public XmlAuthorizingRealm()
    {
        setCredentialsMatcher( new Sha1CredentialsMatcher() );
        setAuthorizationCache( new HashtableCache( null ) );
    }

    @Override
    public String getName()
    {
        return XmlAuthorizingRealm.class.getName();
    }

    @Override
    public boolean supports( AuthenticationToken token )
    {
        return false;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token )
        throws AuthenticationException
    {
        return null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo( PrincipalCollection principals )
    {
        if ( principals == null )
        {
            throw new AuthorizationException( "Cannot authorize with no principals." );
        }

        String username = (String) principals.iterator().next();

        PlexusUser user = this.userManager.getUser( username );
        
        if ( user == null )
        {
            throw new AuthorizationException( "User '" + username + "' cannot be retrieved." );
        }

        // FIXME: This should use the RoleResolver, its to late in the release cycle to change it now,
        // but it will be something simple like:
        //
        // for( String permission : roleResolver.resolvePermissions( user.getRoles() )
        // {
        //    permissions.add( new WildcardPermission( permission );
        // }
        //
        
        LinkedList<String> rolesToProcess = new LinkedList<String>();
        Set<PlexusRole> roles = user.getRoles();
        
        if ( roles != null )
        {
            for ( PlexusRole plexusRole : roles )
            {
                if ( plexusRole != null
                    && StringUtils.isNotEmpty( plexusRole.getRoleId() ) )
                {
                    rolesToProcess.add( plexusRole.getRoleId() );
                }
            }
        }

        Set<String> roleIds = new LinkedHashSet<String>();
        Set<Permission> permissions = new LinkedHashSet<Permission>();
        while ( !rolesToProcess.isEmpty() )
        {
            String roleId = rolesToProcess.removeFirst();
            if ( !roleIds.contains( roleId ) )
            {
                CRole role;
                try
                {
                    role = configuration.readRole( roleId );
                    roleIds.add( roleId );

                    // process the roles this role has
                    rolesToProcess.addAll( role.getRoles() );

                    // add the permissions this role has
                    List<String> privilegeIds = role.getPrivileges();
                    for ( String privilegeId : privilegeIds )
                    {
                        Set<Permission> set = getPermissions( privilegeId );
                        permissions.addAll( set );
                    }
                }
                catch ( NoSuchRoleException e )
                {
                    // skip
                }
            }
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo( roleIds );
        info.setObjectPermissions( permissions );

        return info;
    }

    protected Set<Permission> getPermissions( String privilegeId )
    {
        try
        {
            CPrivilege privilege = getConfigurationManager().readPrivilege( privilegeId );
            
            for ( PrivilegeDescriptor descriptor : privilegeDescriptors )
            {
                String permission = descriptor.buildPermission( privilege );
                
                if ( permission != null )
                {
                    return Collections.singleton( (Permission) new WildcardPermission( permission ) );
                }
            }
            
            return Collections.emptySet();
        }
        catch ( NoSuchPrivilegeException e )
        {
            return Collections.emptySet();
        }
    }

    protected ConfigurationManager getConfigurationManager()
    {
        return configuration;
    }
}