package com.pontoapp.pontoapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pontoapp.pontoapp.dto.NewUserDTO;
import com.pontoapp.pontoapp.dto.UpdateUserDTO;
import com.pontoapp.pontoapp.dto.UserDTO;
import com.pontoapp.pontoapp.entity.User;
import com.pontoapp.pontoapp.exceptions.UserServiceException;
import com.pontoapp.pontoapp.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void insert(NewUserDTO userDTO) {
        try {
            validateUser(userDTO);
            validatePassword(userDTO);
            User user = new User(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setId(null);
            userRepository.save(user);
        } catch (Exception e) {
            throw new UserServiceException("Erro ao criar novo usuário", e);
        }
    }

    public UserDTO update(UpdateUserDTO userDTO) {
        try {
            User existingUser = userRepository.findById(userDTO.getId()).get();
            if (existingUser == null) {
                throw new UserServiceException("Usuário não encontrado para o id fornecido: " + userDTO.getId());
            }

            // Atualiza os campos do usuário com os valores do DTO, exceto a senha
            existingUser.setLogin(userDTO.getLogin());
            existingUser.setPassword(userDTO.getPassword());

            // Salva as alterações no usuário
            User updatedUser = userRepository.save(existingUser);

            // Retorna uma visualização do usuário atualizado
            return new UserDTO(updatedUser);
        } catch (Exception e) {
            throw new UserServiceException("Erro ao atualizar usuário", e);
        }
    }

    public List<UserDTO> findAll() {
        try {
            List<User> userList = userRepository.findAll();
            return userList.stream().map(UserDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            throw new UserServiceException("Erro ao buscar usuários", e);
        }
    }

    public void delete(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new UserServiceException("Usuário não encontrado para o ID: " + id));
            userRepository.delete(user);
        } catch (Exception e) {
            throw new UserServiceException("Erro ao excluir usuário", e);
        }
    }

    public UserDTO findById(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new UserServiceException("Usuário não encontrado para o ID: " + id));
            return new UserDTO(user);
        } catch (Exception e) {
            throw new UserServiceException("Erro ao buscar usuário por ID", e);
        }
    }

    private void validateUser(NewUserDTO user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new UserServiceException("Usuário já cadastrado: " + user.getLogin());
        }
    }

    private static void validatePassword(NewUserDTO userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new UserServiceException("As senhas devem ser iguais");
        }
    }
}
