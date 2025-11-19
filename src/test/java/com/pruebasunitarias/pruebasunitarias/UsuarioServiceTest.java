package com.pruebasunitarias.pruebasunitarias;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void registrarUsuario_CuandoNoExisteEmail_DebeGuardar() {
        Usuario nuevo = new Usuario(null, "test@test.com", "Juan");

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(nuevo);

        Usuario resultado = usuarioService.registrarUsuario(nuevo);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(usuarioRepository, times(1)).save(nuevo);
    }

    @Test
    void registrarUsuario_CuandoExisteEmail_DebeLanzarExcepcion() {
        Usuario existente = new Usuario(1L, "dup@test.com", "Ana");

        when(usuarioRepository.findByEmail("dup@test.com"))
                .thenReturn(Optional.of(existente));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.registrarUsuario(existente));

        assertEquals("El usuario ya existe", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void buscarPorId_CuandoExiste_RetornaUsuario() {
        Usuario u = new Usuario(2L, "ok@test.com", "Pedro");

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(u));

        Usuario resultado = usuarioService.buscarPorId(2L);

        assertEquals("Pedro", resultado.getNombre());
    }

    @Test
    void buscarPorId_CuandoNoExiste_LanzaExcepcion() {
        when(usuarioRepository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> usuarioService.buscarPorId(50L));
    }
}
