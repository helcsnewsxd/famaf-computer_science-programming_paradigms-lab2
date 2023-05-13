package namedEntity.heuristic;

import java.util.Map;
import static java.util.Map.entry;

import namedEntity.entities.NamedEntity;
import namedEntity.entities_themes.CityInternational;
import namedEntity.entities_themes.CompanyOtherThemes;
import namedEntity.entities_themes.CountryInternational;
import namedEntity.entities_themes.FirstnameMusic;
import namedEntity.entities_themes.LastnameCinema;
import namedEntity.entities_themes.LastnameCulture;
import namedEntity.entities_themes.LastnameFootball;
import namedEntity.entities_themes.LastnameInternational;
import namedEntity.entities_themes.LastnameTennis;
import namedEntity.entities_themes.OtherEntityNational;

public abstract class Heuristic {

    private static Map<String, Class<? extends NamedEntity>> categoryMap = Map.ofEntries(
            entry("Dybala", LastnameFootball.class),
            entry("Apple", CompanyOtherThemes.class),
            entry("Google", CompanyOtherThemes.class),
            entry("Musk", LastnameCulture.class),
            entry("Biden", LastnameInternational.class),
            entry("Trump", LastnameInternational.class),
            entry("Messi", LastnameFootball.class),
            entry("Federer", LastnameTennis.class),
            entry("USA", CountryInternational.class),
            entry("Russia", CountryInternational.class),
            entry("Twitter", CompanyOtherThemes.class),
            entry("FBI", OtherEntityNational.class),
            entry("Pitt", LastnameCinema.class),
            entry("Reynolds", LastnameCinema.class),
            entry("Franchella", LastnameCinema.class),
            entry("IBM", CompanyOtherThemes.class),
            entry("Tini", FirstnameMusic.class),
            entry("Tiktok", CompanyOtherThemes.class),
            entry("Beijing", CityInternational.class),
            entry("Washington", CityInternational.class),
            entry("Taiwan", CountryInternational.class)
            );

    public Class<? extends NamedEntity> getCategory(String entity) {
        return categoryMap.get(entity);
    }

    public abstract boolean isEntity(String word);

}
