package org.carlspring.strongbox.controllers.users;

import org.carlspring.strongbox.users.domain.AccessModel;
import org.carlspring.strongbox.users.domain.User;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;

/**
 * @author Przemyslaw Fusik
 * @author Pablo Tirado
 * @JsonInclude used because org.carlspring.strongbox.users.domain.User is annotated with it
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserOutput implements Serializable
{
    private String username;

    private boolean enabled;

    private Set<String> roles;

    private String securityTokenKey;

    private AccessModelOutput accessModel;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public Set<String> getRoles()
    {
        return roles == null ? Collections.emptySet() : ImmutableSet.copyOf(roles);
    }

    public void setRoles(Set<String> roles)
    {
        this.roles = roles;
    }

    public String getSecurityTokenKey()
    {
        return securityTokenKey;
    }

    public void setSecurityTokenKey(String securityTokenKey)
    {
        this.securityTokenKey = securityTokenKey;
    }

    public AccessModelOutput getAccessModel()
    {
        return accessModel;
    }

    public void setAccessModel(AccessModelOutput accessModel)
    {
        this.accessModel = accessModel;
    }

    static UserOutput fromUser(User user)
    {
        final UserOutput output = new UserOutput();
        output.setEnabled(user.isEnabled());
        output.setRoles(user.getRoles());
        output.setUsername(user.getUsername());
        output.setAccessModel(new AccessModelOutput(user.getAccessModel()));
        output.setSecurityTokenKey(user.getSecurityTokenKey());
        return output;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("UserOutput{");
        sb.append("username='").append(username).append('\'');
        sb.append(", enabled=").append(enabled);
        sb.append(", roles=").append(roles);
        sb.append(", securityTokenKey='").append(securityTokenKey).append('\'');
        sb.append(", accessModel=").append(accessModel);
        sb.append('}');
        return sb.toString();
    }
}
