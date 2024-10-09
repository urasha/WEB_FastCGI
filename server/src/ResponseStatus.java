public enum ResponseStatus {
    SUCCESS(true, true),
    VALIDATION_FAILED(false, false),
    HIT_FAILED(false, true);

    private final boolean isHit;
    private final boolean isValidated;

    ResponseStatus(boolean isHit, boolean isValidated) {
        this.isHit = isHit;
        this.isValidated = isValidated;
    }

    public boolean isHit() {
        return isHit;
    }

    public boolean isValidated() {
        return isValidated;
    }
}
