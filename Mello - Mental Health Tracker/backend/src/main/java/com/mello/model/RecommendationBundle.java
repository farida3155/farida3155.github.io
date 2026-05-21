package com.mello.model;

import java.util.List;

public class RecommendationBundle {
    private final List<SuggestionItem> suggestions;
    private final List<SuggestionItem> smartSuggestions;
    private final List<SuggestionItem> microActions;
    private final List<String> weeklyPlan;
    private final String gentleRiskAlert;
    private final List<String> positiveReinforcement;

    public RecommendationBundle(
        List<SuggestionItem> suggestions,
        List<SuggestionItem> smartSuggestions,
        List<SuggestionItem> microActions,
        List<String> weeklyPlan,
        String gentleRiskAlert,
        List<String> positiveReinforcement
    ) {
        this.suggestions = suggestions;
        this.smartSuggestions = smartSuggestions;
        this.microActions = microActions;
        this.weeklyPlan = weeklyPlan;
        this.gentleRiskAlert = gentleRiskAlert;
        this.positiveReinforcement = positiveReinforcement;
    }

    public List<SuggestionItem> getSuggestions() {
        return suggestions;
    }

    public List<SuggestionItem> getSmartSuggestions() {
        return smartSuggestions;
    }

    public List<SuggestionItem> getMicroActions() {
        return microActions;
    }

    public List<String> getWeeklyPlan() {
        return weeklyPlan;
    }

    public String getGentleRiskAlert() {
        return gentleRiskAlert;
    }

    public List<String> getPositiveReinforcement() {
        return positiveReinforcement;
    }
}
