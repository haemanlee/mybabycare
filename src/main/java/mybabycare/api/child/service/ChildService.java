package mybabycare.api.child.service;

import mybabycare.api.child.dto.ChildRequest;
import mybabycare.api.child.dto.ChildResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ChildService {
    private final AtomicLong sequence = new AtomicLong(1);
    private final Map<Long, ChildResponse> children = new LinkedHashMap<>();

    public List<ChildResponse> findAll() {
        return new ArrayList<>(children.values());
    }

    public ChildResponse create(ChildRequest request) {
        long id = sequence.getAndIncrement();
        ChildResponse response = new ChildResponse(id, request.name(), request.birthday());
        children.put(id, response);
        return response;
    }

    public ChildResponse findById(Long childId) {
        return children.get(childId);
    }

    public ChildResponse update(Long childId, ChildRequest request) {
        ChildResponse response = new ChildResponse(childId, request.name(), request.birthday());
        children.put(childId, response);
        return response;
    }

    public void delete(Long childId) {
        children.remove(childId);
    }
}
