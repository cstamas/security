package org.sonatype.security.authorization;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple bean that represents a privilge.
 * 
 * @author Brian Demers
 */
public class Privilege
{

    /**
     * Field id
     */
    private String id;

    /**
     * Field name
     */
    private String name;

    /**
     * Field description
     */
    private String description;

    /**
     * Field type
     */
    private String type;

    /**
     * Field properties
     */
    private Map<String, String> properties = new HashMap<String, String>();

    private boolean readOnly;
    
    public Privilege()
    {
        
    }
    
    public Privilege( String id, String name, String description, String type, Map<String, String> properties,
                      boolean readOnly )
    {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.properties = properties;
        this.readOnly = readOnly;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void addProperty( String key, String value )
    {
        this.properties.put( key, value );
    }

    public void setProperties( Map<String, String> properties )
    {
        this.properties = properties;
    }

    public boolean isReadOnly()
    {
        return readOnly;
    }

    public void setReadOnly( boolean readOnly )
    {
        this.readOnly = readOnly;
    }

    public String getPrivilegeProperty( String key )
    {
        return this.properties.get( key );
    }

}
