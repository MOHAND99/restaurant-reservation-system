package adapters;

import adapted.AdaptedDish;
import restaurant.dish.Appetizer;
import restaurant.dish.Desert;
import restaurant.dish.Dish;
import restaurant.dish.MainCourse;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DishAdapter extends XmlAdapter<AdaptedDish, Dish> {
    @Override
    public Dish unmarshal(AdaptedDish v) throws Exception {
        if (v.getType().equals("main_course")) return new MainCourse(v.getName(), v.getPrice());
        if (v.getType().equals("appetizer")) return new Appetizer(v.getName(), v.getPrice());
        if (v.getType().equals("desert")) return new Desert(v.getName(), v.getPrice());
        throw new IllegalArgumentException("Dish type isn't valid.");
    }

    @Override
    public AdaptedDish marshal(Dish v) throws Exception {
        AdaptedDish adaptedDish = new AdaptedDish();
        adaptedDish.setName(v.getName());
        adaptedDish.setPrice(v.getPrice());
        if (v instanceof MainCourse) {
            adaptedDish.setType("main_course");
        } else if (v instanceof Appetizer) {
            adaptedDish.setType("appetizer");
        } else if (v instanceof Desert) {
            adaptedDish.setType("desert");
        } else {
            throw new IllegalArgumentException("Dish type isn't found. This exception indicates a bug in DishAdapter.");
        }
        return adaptedDish;
    }
}
