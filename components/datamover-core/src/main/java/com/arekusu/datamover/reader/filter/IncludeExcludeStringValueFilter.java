package com.arekusu.datamover.reader.filter;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;

import java.util.List;

public class IncludeExcludeStringValueFilter implements EntityFilter {

    List<String> includes;
    List<String> excludes;
    String filterFieldAlias;

    @Override
    public boolean isValidEntity(Entity entity) {
        boolean result = true;
        for (Field field : entity.getFields()) {
            if (field.getType().getAlias().equals(filterFieldAlias)) {
                String value = field.getValue();
                if ((includes != null && !includes.isEmpty() && !includes.contains(value)) || (excludes != null && excludes.contains(value))) {
                    result = false;
                }
            }
        }
        return result;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public String getFilterFieldAlias() {
        return filterFieldAlias;
    }

    public void setFilterFieldAlias(String filterFieldAlias) {
        this.filterFieldAlias = filterFieldAlias;
    }
}
