package ru.yandex.practicum.filmorate.model;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Mpa extends ListModel{
    private static final Map<Integer,String> mp = new HashMap<>() {{
        put(1,"G");
        put(2,"PG");
        put(3,"PG-13");
        put(4,"R");
        put(5,"NC-17");
        put(0,null);
    }};

    public  Mpa(){

    }
    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Mpa(int id) {
        this.id = id;
        this.name = getNameByID(id,mp);
    }

    public Mpa(String name){
        if (getIdByName(name,mp).isPresent()) {
            this.id = getIdByName(name,mp).get();
            this.name = name;
        }
    }
}
