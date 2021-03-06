package org.sonatype.security.usermanagement;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.plexus.util.CollectionUtils;
import org.codehaus.plexus.util.StringUtils;

/**
 * An abstract UserManager that handles filtering UserSearchCriteria in memory, this can be used in addition to an
 * external query ( if all the parameters can not be pased to the external source).
 * 
 * @author Brian Demers
 */
public abstract class AbstractUserManager
    implements UserManager
{

    protected Set<User> filterListInMemeory( Set<User> users, UserSearchCriteria criteria )
    {
        HashSet<User> result = new HashSet<User>();

        for ( User user : users )
        {
            if ( userMatchesCriteria( user, criteria ) )
            {
                // add the user if it matches the search criteria
                result.add( user );
            }
        }

        return result;
    }

    protected boolean userMatchesCriteria( User user, UserSearchCriteria criteria )
    {
        Set<String> userRoles = new HashSet<String>();
        if ( user.getRoles() != null )
        {
            for ( RoleIdentifier roleIdentifier : user.getRoles() )
            {
                userRoles.add( roleIdentifier.getRoleId() );
            }
        }

        return matchesCriteria( user.getUserId(), user.getSource(), userRoles, criteria );
    }

    protected boolean matchesCriteria( String userId, String userSource, Collection<String> usersRoles,
                                       UserSearchCriteria criteria )
    {
        if ( StringUtils.isNotEmpty( criteria.getUserId() )
            && !userId.toLowerCase().startsWith( criteria.getUserId().toLowerCase() ) )
        {
            return false;
        }

        if ( criteria.getSource() != null && !criteria.getSource().equals( userSource ) )
        {
            return false;
        }

        if ( criteria.getOneOfRoleIds() != null && !criteria.getOneOfRoleIds().isEmpty() )
        {
            Set<String> userRoles = new HashSet<String>();
            if ( usersRoles != null )
            {
                userRoles.addAll( usersRoles );
            }

            // check the intersection of the roles
            if ( CollectionUtils.intersection( criteria.getOneOfRoleIds(), userRoles ).isEmpty() )
            {
                return false;
            }
        }

        return true;
    }

}
