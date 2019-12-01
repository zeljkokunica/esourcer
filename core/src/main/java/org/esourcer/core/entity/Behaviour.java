package org.esourcer.core.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Behaviour<Command extends ReplyType, Event, Entity> {

    private final Map<Class<? extends ReplyType>, Function> commandHandlers = new HashMap<>();
    private final Map<Class<? extends Event>, BiFunction> eventHandlers = new HashMap<>();

    public <Reply, RealCommand extends ReplyType, RealEvent extends Event> Behaviour setCommandHandler(
            final Class<RealCommand> command,
            final Function<RealCommand, ResultAndEvents<Reply, RealEvent>> commandHandler) {
        commandHandlers.put(command, commandHandler);
        return this;
    }

    public <RealEvent extends Event> Behaviour setEventHandler(final Class<RealEvent> event,
            final BiFunction<Optional<Entity>, RealEvent, Optional<Entity>> eventHandler) {
        eventHandlers.put(event, eventHandler);
        return this;
    }

    public boolean handlesCommand(final Class<? extends Command> command) {
        return commandHandlers.containsKey(command);
    }

    public boolean handlesEvent(final Class<? extends Event> event) {
        return eventHandlers.containsKey(event);
    }

    public <Result> Function<? extends Command, ResultAndEvents<Result, Event>> commandHandler(
            final Class<? extends Command> command) {
        return commandHandlers.get(command);
    }

    public <RealEvent extends Event> BiFunction<Optional<Entity>, RealEvent, Optional<Entity>> eventHandler(
            final Class<RealEvent> event) {
        return eventHandlers.get(event);
    }

}
