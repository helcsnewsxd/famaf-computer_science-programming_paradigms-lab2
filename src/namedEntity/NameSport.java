package namedEntity;

import namedEntity.theme.Sport;

public class NameSport extends Name implements Sport {

    public NameSport(String name, String category, int frequency) {
        super(name, category, frequency);
        //TODO Auto-generated constructor stub
    }

    public NameSport() {
        super();
    }

    public String getType() {
        return "Nombre";
    }


    
}
