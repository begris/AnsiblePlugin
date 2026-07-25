package ir.msdehghan.plugins.ansible.model.ansible.task;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

class AnsiblePluginsDto {

    @JsonProperty("plugins")
    List<Plugin> plugins;

    static class Plugin {
        @JsonProperty("name")
        String name;

        @JsonProperty("fqcn")
        String fqcn;

        @JsonProperty("collection")
        public String collection;
    }

}
