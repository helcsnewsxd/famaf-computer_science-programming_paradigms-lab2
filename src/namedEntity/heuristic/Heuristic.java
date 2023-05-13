package namedEntity.heuristic;

import java.util.Map;

import namedEntity.entities.NamedEntity;
import namedEntity.entities_themes.LastnameFootball;

public abstract class Heuristic {

    private static Map<String, Class<? extends NamedEntity>> categoryMap = Map.of(
            "Dybala", LastnameFootball.class
            // "Apple", "Company",
            // "Google", "Company",
            // "Musk", new PersonTema(),
            // "Biden", new PersonPolitics(),
            // "Trump", "Person",
            // "Messi", "Person",
            // "Federer", "Person",
            // "USA", "Country",
            // "Russia", "Country"
            );

    public Class<? extends NamedEntity> getCategory(String entity) {
        return categoryMap.get(entity);
    }

    public abstract boolean isEntity(String word);

}
