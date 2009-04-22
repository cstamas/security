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
package org.sonatype.security.configuration.upgrade;

import java.io.File;
import java.io.IOException;


/**
 * A marker interface for security upgraders.
 * 
 * @author cstamas
 */
public interface SecurityUpgrader
{
    Object loadConfiguration( File file )
    throws IOException,
        ConfigurationIsCorruptedException;

    void upgrade( UpgradeMessage message )
    throws ConfigurationIsCorruptedException;
}