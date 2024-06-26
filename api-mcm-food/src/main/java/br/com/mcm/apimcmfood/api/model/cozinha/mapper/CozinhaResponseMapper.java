package br.com.mcm.apimcmfood.api.model.cozinha.mapper;

import br.com.mcm.apimcmfood.api.model.cozinha.CozinhaListResponse;
import br.com.mcm.apimcmfood.api.model.cozinha.CozinhaResponse;
import br.com.mcm.apimcmfood.domain.entity.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CozinhaResponseMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CozinhaResponse toResponse(Cozinha cozinha) {
        return modelMapper.map(cozinha, CozinhaResponse.class);
    }

    public CozinhaListResponse toListResponse(Cozinha cozinha) {
        return modelMapper.map(cozinha, CozinhaListResponse.class);
    }

    public List<CozinhaListResponse> toCollectionResponse(List<Cozinha> cozinhas) {
        return cozinhas.stream()
                .map(cozinha -> toListResponse(cozinha))
                .collect(Collectors.toList());
    }

    public Page<CozinhaListResponse> toPageableToResponse(Page<Cozinha> cozinhas) {
        List<CozinhaListResponse> pedidoList = cozinhas.getContent().stream()
                .map(pedido -> toListResponse(pedido))
                .collect(Collectors.toList());

        return new PageImpl<>(pedidoList, cozinhas.getPageable(), cozinhas.getTotalElements());
    }
}
