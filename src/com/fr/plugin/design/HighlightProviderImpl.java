package com.fr.plugin.design;

import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.fun.impl.AbstractHighlightProvider;

public class HighlightProviderImpl extends AbstractHighlightProvider {
    @Override
    public Class<?> classForHighlightAction() {
        return null;
    }

    @Override
    public ConditionAttrSingleConditionPane appearanceForCondition(ConditionAttributesPane conditionAttributesPane) {
        return null;
    }
}