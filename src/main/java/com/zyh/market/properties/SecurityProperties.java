package com.zyh.market.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private Boolean enabled = true;
    private List<String> whiteList = new ArrayList<>();
}
