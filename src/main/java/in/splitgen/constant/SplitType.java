package in.splitgen.constant;

/**
 * Defines the methods used to calculate expense splits.
 * Corresponds to the 'split_type' ENUM in the SQL schema.
 */
public enum SplitType {
    /** Expense is divided equally among selected participants. */
    EQUAL,
    /** Expense is divided based on specific amounts entered per participant. */
    EXACT,
    /** Expense is divided based on percentages or ratios. */
    PERCENTAGE
}

