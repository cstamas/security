package org.sonatype.security.configuration.validator;

import java.util.List;

import javax.enterprise.inject.Typed;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.configuration.validation.ValidationRequest;
import org.sonatype.configuration.validation.ValidationResponse;
import org.sonatype.security.configuration.model.SecurityConfiguration;

@Singleton
@Typed( value = SecurityConfigurationValidator.class )
@Named( value = "default" )
public class DefaultSecurityConfigurationValidator
    implements SecurityConfigurationValidator
{

    public ValidationResponse validateModel( SecurityValidationContext context,
        ValidationRequest<SecurityConfiguration> request )
    {
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse.setContext( context );

        SecurityConfiguration configuration = request.getConfiguration();

        validationResponse.append( this.validateAnonymousUsername( context, configuration.getAnonymousUsername() ) );
        validationResponse.append( this.validateAnonymousPassword( context, configuration.getAnonymousPassword() ) );
        validationResponse.append( this.validateRealms( context, configuration.getRealms() ) );

        return validationResponse;
    }

    public ValidationResponse validateAnonymousPassword( SecurityValidationContext context,
        String anonymousPassword )
    {
        // we are not currently doing anything here
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse.setContext( context );
        return validationResponse;
    }

    public ValidationResponse validateAnonymousUsername( SecurityValidationContext context,
        String anonymousUsername )
    {
        // we are not currently doing anything here
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse.setContext( context );
        return validationResponse;
    }

    public ValidationResponse validateRealms( SecurityValidationContext context,
        List<String> realms )
    {
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse.setContext( context );

        if ( ( context.getSecurityConfiguration() != null && context.getSecurityConfiguration().isEnabled() )
            || context.getSecurityConfiguration() == null )
        {
            if ( realms.size() < 1 )
            {
                validationResponse
                    .addValidationError( "Security is enabled, You must have at least one realm enabled." );
            }
            // TODO: we should also try to load each one to see if it exists
        }

        return validationResponse;
    }

}
