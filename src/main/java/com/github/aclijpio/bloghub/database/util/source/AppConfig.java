package com.github.aclijpio.bloghub.database.util.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppConfig {
    private Datasource datasource;
}
