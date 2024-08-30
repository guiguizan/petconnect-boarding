package br.com.petconnect.boarding.service.user;


import br.com.petconnect.boarding.domain.Role;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.repositories.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;



    public Role findById(Long roleId){
        return roleRepository.findById(roleId).orElseThrow(
                () ->
                new BusinessException("Role não encontrada")
        );
    }
}
