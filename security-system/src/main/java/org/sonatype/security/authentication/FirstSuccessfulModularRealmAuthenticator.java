package org.sonatype.security.authentication;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Authenticator will only try to authenticate with each realm. The first successful AuthenticationInfo found will
 * be returned and other realms will not be queried. <BR/><BR/>
 * This makes for the performance short comings when using the {@link ModularRealmAuthenticator} and
 * {@link FirstSuccessfulAuthenticationStrategy} where all the realms will be queried, but only the first success is
 * returned.
 * 
 * @author Brian Demers
 * @see ModularRealmAuthenticator
 * @see FirstSuccessfulAuthenticationStrategy
 */
public class FirstSuccessfulModularRealmAuthenticator
    extends ModularRealmAuthenticator
{

    private static final Logger log = LoggerFactory.getLogger( FirstSuccessfulModularRealmAuthenticator.class );

    @Override
    protected AuthenticationInfo doMultiRealmAuthentication( Collection<Realm> realms, AuthenticationToken token )
    {
        log.trace( "Iterating through [" + realms.size() + "] realms for PAM authentication" );

        for ( Realm realm : realms )
        {
            // check if the realm supports this token
            if ( realm.supports( token ) )
            {
                if ( log.isTraceEnabled() )
                {
                    log.trace( "Attempting to authenticate token [" + token + "] " + "using realm of type [" + realm + "]" );
                }
                
                try
                {
                    // try to login
                    AuthenticationInfo info = realm.getAuthenticationInfo( token );
                    // just make sure are ducks are in a row
                    // return the first successful login.
                    if ( info != null )
                    {
                        return info;
                    }
                    else
                    {
                        log.warn( "Realm [" + realm + "] returned null when authenticating token " + "[" + token + "]" );
                    }
                }
                catch ( Throwable t )
                {
                    if ( log.isTraceEnabled() )
                    {
                        String msg = "Realm [" + realm
                            + "] threw an exception during a multi-realm authentication attempt:";
                        log.trace( msg, t );
                    }
                }
            }
            else
            {
                if ( log.isTraceEnabled() )
                {
                    log.trace( "Realm of type [" + realm + "] does not support token " + "[" + token
                        + "].  Skipping realm." );
                }
            }
        }
        throw new org.apache.shiro.authc.AuthenticationException( "Authentication token of type [" + token.getClass() + "] "
            + "could not be authenticated by any configured realms.  Please ensure that at least one realm can "
            + "authenticate these tokens." );
    }
}
