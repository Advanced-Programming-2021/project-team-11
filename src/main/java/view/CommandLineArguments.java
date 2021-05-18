package view;

import com.beust.jcommander.Parameter;

class CommandLineArguments {
    @Parameter(names = {"--monster", "-m"})
    private String monsters = "Monster.csv";
    @Parameter(names = {"--spell", "-s"})
    private String spell = "Spell.csv";
    @Parameter(names = {"--database", "-d"})
    private String database = "database.db";

    public String getMonstersConfigName() {
        return monsters;
    }

    public String getDatabase() {
        return database;
    }

    public String getSpellConfigName() {
        return spell;
    }
}
