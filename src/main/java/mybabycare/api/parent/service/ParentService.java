package mybabycare.api.parent.service;

import mybabycare.api.parent.dto.ParentRequest;
import mybabycare.api.parent.dto.ParentResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ParentService {
    private final AtomicLong sequence = new AtomicLong(1);
    private final Map<Long, ParentResponse> parents = new LinkedHashMap<>();

    public List<ParentResponse> findAll() {
        return new ArrayList<>(parents.values());
    }

    public ParentResponse create(ParentRequest request) {
        long id = sequence.getAndIncrement();
        ParentResponse response = new ParentResponse(id, request.name(), request.phoneNumber());
        parents.put(id, response);
        return response;
    }

    public ParentResponse findById(Long parentId) {
        return parents.get(parentId);
    }

    public ParentResponse update(Long parentId, ParentRequest request) {
        ParentResponse response = new ParentResponse(parentId, request.name(), request.phoneNumber());
        parents.put(parentId, response);
        return response;
    }

    public void delete(Long parentId) {
        parents.remove(parentId);
    }
}
