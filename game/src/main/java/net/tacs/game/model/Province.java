package net.tacs.game.model;

import net.tacs.game.exceptions.NotEnoughMunicipalitiesException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Province {

    private String name;
    private List<Municipality> municipalities;

    public Province(String name) {
        this.name = name;
    }

    public Province(String province, int cant_municipalities)
    {
        this.name = province;

        search_municipalities(cant_municipalities);
    }

    /**
     * @method search_municipalities
     * @param cant_municipalities
     * @throws NotEnoughMunicipalitiesException
     * Pide a la API de municipios que le devuelva una lista de X cantidad municipios,
     * en caso de que la cantidad elegida supere la cantidad actual se envia un mensaje de error
     */
    private void search_municipalities(int cant_municipalities) throws NotEnoughMunicipalitiesException
    {
        //municipalities = MunicipalitiesAPI.GetMunicipalities(cant_municipalities);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Province{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Province province = (Province) o;
        return Objects.equals(name, province.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
