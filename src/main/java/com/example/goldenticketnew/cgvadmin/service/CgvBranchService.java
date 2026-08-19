package com.example.goldenticketnew.cgvadmin.service;

import com.example.goldenticketnew.cgvadmin.dto.request.CgvBranchRequest;
import com.example.goldenticketnew.cgvadmin.dto.response.CgvBranchDTO;
import com.example.goldenticketnew.model.Branch;
import com.example.goldenticketnew.repository.IBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CgvBranchService {

    @Autowired
    private IBranchRepository branchRepository;

    public Page<CgvBranchDTO> getAllBranches(Pageable pageable) {
        return branchRepository.findAll(pageable).map(CgvBranchDTO::new);
    }

    public CgvBranchDTO createBranch(CgvBranchRequest request) {
        Branch branch = new Branch();
        branch.setName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setPhoneNo(request.getPhoneNo());
        branch.setImgURL(request.getImgURL());

        Branch savedBranch = branchRepository.save(branch);
        return new CgvBranchDTO(savedBranch);
    }

    public CgvBranchDTO updateBranch(Integer id, CgvBranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("KhÃ´ng tÃ¬m tháº¥y ráº¡p vá»›i ID: " + id));

        branch.setName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setPhoneNo(request.getPhoneNo());
        branch.setImgURL(request.getImgURL());

        Branch updatedBranch = branchRepository.save(branch);
        return new CgvBranchDTO(updatedBranch);
    }

    public void deleteBranch(Integer id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("KhÃ´ng tÃ¬m tháº¥y ráº¡p vá»›i ID: " + id));
        try {
            branchRepository.delete(branch);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("KhÃ´ng thá»ƒ xÃ³a: ráº¡p nÃ y Ä‘ang cÃ³ phÃ²ng chiáº¿u hoáº·c suáº¥t chiáº¿u liÃªn káº¿t, vui lÃ²ng xÃ³a cÃ¡c dá»¯ liá»‡u con trÆ°á»›c");
        }
    }
}
