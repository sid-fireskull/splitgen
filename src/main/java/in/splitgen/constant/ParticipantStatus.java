package in.splitgen.constant;

/**
 * Defines the possible status states for a user in a specific tour.
 * Corresponds to the 'participant_status' ENUM in the SQL schema.
 */
public enum ParticipantStatus {
    /** The user has been invited via email and has not yet accepted. */
    INVITED,
    /** The user requested to join using the tour code, awaiting admin approval. */
    PENDING_CODE_JOIN,
    REJECTED,
    APPROVED
}

