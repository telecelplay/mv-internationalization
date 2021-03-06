package org.meveo.model.customEntities;

import org.meveo.model.CustomEntity;
import java.util.List;
import org.meveo.model.persistence.DBStorageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.meveo.model.billing.Language;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class LocalizedMessage implements CustomEntity {

    public LocalizedMessage() {
    }

    public LocalizedMessage(String uuid) {
        this.uuid = uuid;
    }

    private String uuid;

    @JsonIgnore()
    private DBStorageType storages;

    @JsonProperty(required = true)
    private String module;

    @JsonProperty(required = true)
    private Language language;

    @JsonProperty(required = true)
    private String value;

    @JsonProperty(required = true)
    private String key;

    @Override()
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public DBStorageType getStorages() {
        return storages;
    }

    public void setStorages(DBStorageType storages) {
        this.storages = storages;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override()
    public String getCetCode() {
        return "LocalizedMessage";
    }
}
