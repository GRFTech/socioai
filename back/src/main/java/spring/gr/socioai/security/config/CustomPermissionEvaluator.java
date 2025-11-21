package spring.gr.socioai.security.config;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import spring.gr.socioai.model.valueobjects.Email;
import spring.gr.socioai.repository.AuthenticatedUserRepository;
import spring.gr.socioai.repository.CategoriaRepository;
import spring.gr.socioai.repository.LancamentoRepository;
import spring.gr.socioai.repository.MetaRepository;

import java.io.Serializable;
import java.util.UUID;

@Component
@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private LancamentoRepository lancRepo;

    @Autowired
    private MetaRepository metaRepo;

    @Autowired
    private CategoriaRepository categoriaRepo;

    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {

        String username = auth.getName();
        UUID userId = authenticatedUserRepository
                .findByUsername(new Email(username))
                .orElseThrow(() -> new EntityNotFoundException("O user com id " + username + " não existe"))
                .getId();

        Long id = Long.parseLong(targetId.toString());

        log.info("Iniciando autorização para o usuário: {}", userId);
        log.info("Verificando se ele pode acessar o recurso: {}", targetType);

        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            log.info("Acesso de administrador liberado para admin com id: {}", userId);
            return true;
        }

        return switch (targetType.toLowerCase()) {
            case "lancamento" -> lancRepo.isOwner(id, userId);
            case "meta"       -> metaRepo.isOwner(id, userId);
            case "categoria"  -> categoriaRepo.existsByIdAndUserId(id, userId);
            default -> false;
        };
    }

    @Override
    public boolean hasPermission(Authentication auth,
                                 Object targetDomainObject,
                                 Object permission) {
        return false;
    }
}
