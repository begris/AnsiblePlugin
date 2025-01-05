package api;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnsiblePluginListDto {

    public List<Plugin> plugins;

    public static class Plugin {
        public String fqcn;
        public String name;
        public String collection;
    }
}
