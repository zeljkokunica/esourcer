package org.esourcer.core.foo;

import org.esourcer.core.EntityFactory;
import org.esourcer.core.foo.FooCommand.CreateFoo;
import org.esourcer.core.foo.FooCommand.GetFoo;
import org.esourcer.core.foo.FooCommand.UpdateFoo;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FooEntityManagerTest {

    @Test
    public void testExecuteCommand() {
        final String fooId = UUID.randomUUID().toString();
        final var fooEntityFactory = new EntityFactory<>(new FooEntity());
        final var fooEntityManager = fooEntityFactory.forEntity(fooId);
        final Instant creationTimestamp = Instant.now();
        final String newFooId = fooEntityManager.executeCommand(new CreateFoo(fooId, "test", creationTimestamp))
                .orElseThrow();
        final Foo newFoo = fooEntityManager.executeCommand(new GetFoo()).orElseThrow();
        assertThat(newFoo.getId()).isEqualTo(fooId);
        assertThat(newFoo.getName()).isEqualTo("test");
        final Instant update1 = Instant.now();
        fooEntityManager.executeCommand(new UpdateFoo("1", update1));
        final Instant update2 = Instant.now();
        fooEntityManager.executeCommand(new UpdateFoo("2", update2));
        final Instant update3 = Instant.now();
        fooEntityManager.executeCommand(new UpdateFoo("3", update3));
        final Instant update4 = Instant.now();
        fooEntityManager.executeCommand(new UpdateFoo("4", update4));
        final Foo recovered = fooEntityManager.executeCommand(new GetFoo()).orElseThrow();
        assertThat(recovered.getId()).isEqualTo(fooId);
        assertThat(recovered.getName()).isEqualTo("4");
        assertThat(recovered.getCreatedAt()).isEqualTo(creationTimestamp);
        assertThat(recovered.getUpdatedAt()).isEqualTo(update4);
    }
}
