package aug.bueno.cloudstorage.services;

import aug.bueno.cloudstorage.dto.CredentialFormDTO;
import aug.bueno.cloudstorage.model.Credential;
import aug.bueno.cloudstorage.repository.mapper.CredentialMapper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<CredentialFormDTO> findAllNotesUser(final int userID) {
        return credentialMapper.getAllCredentialByUser(userID).stream()
                .map(this::credentialToCredentialFormDTO)
                .collect(Collectors.toList());
    }

    public boolean insertOrUpdateCredential(final CredentialFormDTO credentialFormDTOl, final int userID) {

        Credential.CredentialBuilder credentialBuilder = Credential.builder()
                .credentialID(credentialFormDTOl.getCredentialID())
                .url(credentialFormDTOl.getUrl())
                .userName(credentialFormDTOl.getUserName())
                .userID(userID);

        this.setEncryptedPassword(credentialFormDTOl.getPassword(), credentialBuilder);

        if (credentialFormDTOl.getCredentialID() == null || credentialFormDTOl.getCredentialID().toString().equals("") || credentialFormDTOl.getCredentialID() <= 0) {
            return credentialMapper.insert(credentialBuilder.build()) > 0;
        } else {
            credentialBuilder.credentialID(credentialFormDTOl.getCredentialID());
            return credentialMapper.update(credentialBuilder.build()) > 0;
        }
    }

    public boolean deleteByCredentialID(final Integer credentialID) {
        return credentialMapper.delete(credentialID);
    }

    private Credential.CredentialBuilder setEncryptedPassword(final String password, Credential.CredentialBuilder builder) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        builder.key(encodedKey);
        builder.password(encryptionService.encryptValue(password, encodedKey));

        return builder;
    }

    private CredentialFormDTO credentialToCredentialFormDTO(final Credential credential) {
        return CredentialFormDTO.builder()
                .credentialID(credential.getCredentialID())
                .password(credential.getPassword())
                .url(credential.getUrl())
                .key(credential.getKey())
                .userName(credential.getUserName())
                .userID(credential.getUserID())
                .build();
    }
}
