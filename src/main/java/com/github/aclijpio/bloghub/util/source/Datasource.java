package com.github.aclijpio.bloghub.util.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Datasource {
    private String driver;
    private String url;
    private String username;
    private String password;
}
