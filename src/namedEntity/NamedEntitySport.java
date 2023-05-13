package namedEntity;

import namedEntity.theme.Sport;

public class NamedEntitySport extends NamedEntity implements Sport {

    public NamedEntitySport(String name, String category, int frequency) {
        super(name, category, frequency);
        //TODO Auto-generated constructor stub
    }

    public NamedEntitySport() {
    }

    public String themeToString() {
        return "Deporte";
    }
    
}
