package api;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnsibleModuleDto {
    public String fqcn;
    public String name;
    public String collection;
    public String category;
    public Set<String> returnFields;
    public String addedIn;
    public String shortDescription;
    public String description;
    public Deprecated deprecated;
    public String requirements;
    public String notes;
    public Map<String, Field> fields;

    public static class Deprecated {
        public String alternative;
        public String why;
        public String removedIn;
        public String removed_from_collection;
        public String removed_at_date;
    }

    public static class Field {
        public List<String> aliases;
        public List<Choice> choices;
        public String description;
        public String elements;
        public Boolean required;
        public String type;
        public String versionAdded;
        public String defaultValue;

    }

    public static class Choice {
        public String choice;
        public String description;

        public static Builder with() {
            return new Builder();
        }

        public static class Builder {
            private Choice choice;

            private Choice get() {
                if (this.choice == null) {
                    this.choice = new Choice();
                }
                return this.choice;
            }

            public Builder choice(String choice) {
                get().choice = choice;
                return this;
            }

            public Builder description(String description) {
                get().description = description;
                return this;
            }

            public Choice build() {
                return get();
            }
        }
    }
}
