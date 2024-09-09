package br.com.petconnect.boarding.service.user;


import br.com.petconnect.boarding.domain.Role;
import br.com.petconnect.boarding.dto.response.RoleResponseDto;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.exception.ResourceNotFoundException;
import br.com.petconnect.boarding.mapper.UserMapper;
import br.com.petconnect.boarding.repositories.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;



    public Role findById(Long roleId){
        return roleRepository.findById(roleId).orElseThrow(
                () ->
                new ResourceNotFoundException("Role não encontrada")
        );
    }

    public List<RoleResponseDto> getAllRole() {

        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(userMapper::toRoleUser).collect(Collectors.toList());
    }
}
