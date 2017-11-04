package nl.antimeta.bukkit.framework.json;

import nl.antimeta.bukkit.framework.json.enums.SelectorType;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class Selector {

    private SelectorType type;
    private String customSelector;

    private EntityType entityType;
    private int count;
    private GameMode gamemode;
    private boolean gamemodeNotEqual;
    private int levelMinimum;
    private int levelMaximum;
    private String tag;
    private boolean tagNotEqual;
    private Map<String, Integer> scoreMin;
    private Map<String, Integer> scoreMax;

    public Selector(SelectorType type) {
        initialize(type);
    }

    private void initialize(SelectorType type) {
        this.type = type;
        customSelector = null;
        entityType = null;
        count = 0;
        gamemode = null;
        gamemodeNotEqual = false;
        levelMinimum = 0;
        levelMaximum = 0;
        tag = null;
        tagNotEqual = false;
        scoreMin = new HashMap<>();
        scoreMax = new HashMap<>();
    }

    public String buildSelector() {
        if (type == SelectorType.CUSTOM) {
            return customSelector;
        } else {
            SelectorBuilder builder = new SelectorBuilder();
            builder.add(type.name);
            builder.add("[");
            if (entityType != null) {
                builder.addArgument("type", entityType.getName());
            }

            if (count != 0) {
                builder.addArgument("c", count);
            }

            if (gamemode != null) {
                switch (gamemode) {
                    case SURVIVAL:
                        builder.addArgument("m", "survival", gamemodeNotEqual);
                        break;
                    case CREATIVE:
                        builder.addArgument("m", "creative", gamemodeNotEqual);
                        break;
                    case ADVENTURE:
                        builder.addArgument("m", "adventure", gamemodeNotEqual);
                        break;
                    case SPECTATOR:
                        builder.addArgument("m", "spectator", gamemodeNotEqual);
                        break;
                }
            }

            if (levelMinimum != 0) {
                builder.addArgument("lm", levelMinimum);
            }

            if (levelMaximum != 0) {
                builder.addArgument("l", levelMaximum);
            }

            if (tag != null) {
                builder.addArgument("tag", tag, tagNotEqual);
            }

            if (!scoreMin.isEmpty() && !scoreMax.isEmpty()) {

                for (Map.Entry<String, Integer> entry : scoreMin.entrySet()) {
                    String key = entry.getKey();
                    Integer minValue, maxValue;
                    minValue = entry.getValue();
                    maxValue = scoreMax.get(key);

                    builder.addArgument("score_" + key + "_min", minValue);
                    builder.addArgument("score_" + key + "_max", maxValue);
                }
            }

            builder.add("]");
            return builder.getResult();
        }
    }

    public void setCustomSelector(String customSelector) {
        this.customSelector = customSelector;
    }

    public void setType(SelectorType type) {
        this.type = type;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setGamemode(GameMode gamemode) {
        this.gamemode = gamemode;
    }

    public void setLevelMinimum(int levelMinimum) {
        this.levelMinimum = levelMinimum;
    }

    public void setLevelMaximum(int levelMaximum) {
        this.levelMaximum = levelMaximum;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setGamemodeNotEqual(boolean gamemodeNotEqual) {
        this.gamemodeNotEqual = gamemodeNotEqual;
    }

    public void setTagNotEqual(boolean tagNotEqual) {
        this.tagNotEqual = tagNotEqual;
    }

    public void setScore(String name, int min, int max) {
        scoreMin.put("score_" + name + "_min", min);
        scoreMax.put("score_" + name + "_max", max);
    }

    private class SelectorBuilder {

        private String result;
        private boolean first;

        public void add(String addition) {
            result += addition;
        }

        public void addArgument(String key, Object value) {
            if (first) {
                result += (key + "=" + value);
                first = false;
            } else {
                result += ("," + key + "=" + value);
            }
        }

        public void addArgument(String key, Object value, boolean notEqual) {
            if (notEqual) {
                if (first) {
                    result += (key + "=!" + value);
                    first = false;
                } else {
                    result += ("," + key + "=!" + value);
                }
            } else {
                addArgument(key, value);
            }
        }

        public String getResult() {
            return result;
        }
    }
}
