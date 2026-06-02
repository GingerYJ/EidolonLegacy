package elucent.eidolon.compat.crafttweaker;

import crafttweaker.IAction;

abstract class NamedAction implements IAction {
    private final String description;

    NamedAction(String description) {
        this.description = description;
    }

    @Override
    public String describe() {
        return description;
    }
}
