package com.todolist.todolistbackend.mapper;

import com.todolist.todolistbackend.dto.TodoDto;
import com.todolist.todolistbackend.model.Todo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.text.ParseException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTodoFromDto(TodoDto dto, @MappingTarget Todo entity);

    List<TodoDto> todosListToDto(List<Todo> todos);

    TodoDto convertToDto(Todo todo);

    Todo convertToEntity(TodoDto dto) throws ParseException;
}
