/**
 * Copyright (c) 2008 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.security.realms.privileges.application;

import javax.enterprise.inject.Typed;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.security.realms.privileges.PrivilegePropertyDescriptor;

@Singleton
@Typed( value = PrivilegePropertyDescriptor.class )
@Named( value = "ApplicationPrivilegePermissionPropertyDescriptor" )
public class ApplicationPrivilegePermissionPropertyDescriptor
    implements PrivilegePropertyDescriptor
{
    public static final String ID = "permission";
    
    public String getHelpText()
    {
        return "The Shiro permission string associated with this privilege";
    }

    public String getId()
    {
        return ID;
    }

    public String getName()
    {
        return "Permission";
    }
    
    public String getType()
    {
        return "string";
    }

}
