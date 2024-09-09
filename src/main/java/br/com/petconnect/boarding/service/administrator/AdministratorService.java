package br.com.petconnect.boarding.service.administrator;


import br.com.petconnect.boarding.domain.Role;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.UpdateRoleRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.service.user.RoleService;
import br.com.petconnect.boarding.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdministratorService {
    private final UserService userService;
    private final RoleService roleService;

    @Transactional
    public DefaultMessageDto updateEmployeeRole(UpdateRoleRequestDto updateRoleRequestDto){
        User user = userService.findByEmail(updateRoleRequestDto.getEmail());
        Role role = roleService.findById(updateRoleRequestDto.getIdRole());

        user.getRoles().clear();
        user.getRoles().add(role);


        userService.saveUser(user);

        return new DefaultMessageDto("Role atualizada com sucesso");
    }
}
