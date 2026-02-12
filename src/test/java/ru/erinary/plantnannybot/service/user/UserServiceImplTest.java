package ru.erinary.plantnannybot.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.erinary.plantnannybot.api.model.UserModel;
import ru.erinary.plantnannybot.dao.UserRepository;
import ru.erinary.plantnannybot.entity.User;
import ru.erinary.plantnannybot.service.exceptions.EntityAlreadyExistsException;
import ru.erinary.plantnannybot.service.user.mapper.UserMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserServiceImpl.class, UserMapper.class})
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @MockitoBean
    private UserRepository repository;

    @Test
    void testSaveSuccess() {
        Mockito.when(repository.findByTgUserId(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        var model = new UserModel(1L, 2L);
        Mockito.when(repository.save(Mockito.any())).thenReturn(new User(model.tgUserId(), model.chatId()));
        userService.save(model);
        Mockito.verify(repository).save(Mockito.any());
    }

    @Test()
    void testSaveAlreadyExists() {
        Mockito.when(repository.findByTgUserId(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(1L, 2L)));
        var model = new UserModel(1L, 2L);
        assertThrows(EntityAlreadyExistsException.class, () -> userService.save(model));
    }

}