package org.example.first_unit.data_center.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {

    private String id;
    private String weatherData;

}
