package adapters;

import adapted.AdaptedUser;
import restaurant.user.*;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class UserAdapter extends XmlAdapter<AdaptedUser, User> {
    @Override
    public User unmarshal(AdaptedUser v) throws Exception {
        if (v.getRole().equals("Client")) return new Client(v.getUsername(), v.getPassword(), v.getName());
        if (v.getRole().equals("Manager")) return new Manager(v.getUsername(), v.getPassword(), v.getName());
        if (v.getRole().equals("Cooker")) return new Cooker(v.getUsername(), v.getPassword(), v.getName());
        if (v.getRole().equals("Waiter")) return new Waiter(v.getUsername(), v.getPassword(), v.getName());
        throw new IllegalArgumentException("Role isn't valid.");
    }

    @Override
    public AdaptedUser marshal(User v) throws Exception {
        AdaptedUser adaptedUser = new AdaptedUser();
        adaptedUser.setRole(v.getClass().getSimpleName());
        adaptedUser.setName(v.getName());
        adaptedUser.setUsername(v.getUsername());
        adaptedUser.setPassword(v.getPassword());
        return adaptedUser;
    }
}
