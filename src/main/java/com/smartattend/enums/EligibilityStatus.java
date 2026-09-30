package com.smartattend.enums;

public enum EligibilityStatus {
    ELIGIBLE,            // >= 75%
    CONDONABLE_MEDICAL,  // 60% - 74.99% (Subject to Principal condonation under BEU App-I)
    SHORTAGE             // < 60% (Debarred from exam)
}
