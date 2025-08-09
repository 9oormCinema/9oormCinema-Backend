package gc.goormcinema.domain.events.entity;

import lombok.Getter;

@Getter
public class Venue {
    private final String name;
    private final String address;

    public Venue(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
