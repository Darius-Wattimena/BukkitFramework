package nl.antimeta.bukkit.framework.database.model;

public class DatabaseConnectionResource {
    private String hostname;
    private String port;
    private String database;
    private String user;
    private String password;
    private String jdbcParameters = "?useSSL=false&autoReconnect=true&useUnicode=yes";

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcParameters() {
        return jdbcParameters;
    }

    public void setJdbcParameters(String jdbcParameters) {
        this.jdbcParameters = jdbcParameters;
    }
}
