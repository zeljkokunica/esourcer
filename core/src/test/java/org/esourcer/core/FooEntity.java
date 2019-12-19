package org.esourcer.core;

import org.esourcer.core.entity.Behaviour;
import org.esourcer.core.entity.EntityManager;
import org.esourcer.core.entity.PersistentEntity;
import org.esourcer.core.entity.ResultAndEvents;
import org.esourcer.core.FooCommand.CreateFoo;
import org.esourcer.core.FooCommand.GetFoo;
import org.esourcer.core.FooCommand.UpdateFoo;
import org.esourcer.core.FooEvent.FooCreated;
import org.esourcer.core.FooEvent.FooDetails;
import org.esourcer.core.FooEvent.FooUpdated;

import java.util.Optional;

public class FooEntity implements PersistentEntity<FooCommand, FooEvent, Foo, String> {

    @Override
    public String name() {
        return "foo";
    }

    @Override
    public Behaviour<FooCommand, FooEvent, Foo> buildBehaviour(
            final EntityManager<FooCommand, FooEvent, Foo, String> entityManager,
            final Behaviour<FooCommand, FooEvent, Foo> b) {
        b.setCommandHandler(GetFoo.class,
                cmd -> ResultAndEvents.ofResult(entityManager.getEntity().orElse(null)));

        b.setCommandHandler(CreateFoo.class, this::onCreateCommand);
        b.setEventHandler(FooCreated.class, this::onFooCreatedEvent);

        b.setCommandHandler(UpdateFoo.class, cmd -> {
            final Foo existing = entityManager.getEntity().orElseThrow();
            return ResultAndEvents
                    .ofEvent(new FooUpdated(new FooDetails(
                            existing.getId(),
                            cmd.getName(),
                            existing.getCreatedAt(),
                            cmd.getUpdatedAt())));
        });
        b.setEventHandler(FooUpdated.class, this::onFooUpdatedEvent);
        return b;
    }

    private ResultAndEvents<String, FooCreated> onCreateCommand(final CreateFoo command) {
        return ResultAndEvents.of(command.getId(), new FooCreated(
                new FooDetails(
                        command.getId(),
                        command.getName(),
                        command.getCreatedAt(),
                        null)
        ));
    }

    private Optional<Foo> onFooCreatedEvent(final Optional<Foo> entity, final FooCreated event) {
        return Optional
                .of(new Foo(event.getDetails().getId(), event.getDetails().getName(), event.getDetails().getCreatedAt(),
                        null));
    }

    private Optional<Foo> onFooUpdatedEvent(final Optional<Foo> entity, final FooUpdated event) {
        return Optional.of(
                entity.get()
                        .update(
                                event.getDetails().getName(),
                                event.getDetails().getUpdatedAt()));
    }

}
