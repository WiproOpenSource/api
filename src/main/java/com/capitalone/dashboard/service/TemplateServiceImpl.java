package com.capitalone.dashboard.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.Template;
import com.capitalone.dashboard.repository.TemplateRepository;

@Service
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;


    @Autowired
    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public Iterable<Template> all() {
        return templateRepository.findAll();
    }

    @Override
    public Template get(String template) {
        return templateRepository.findByTemplate(template);
    }

    @Override
    public Template create(Template template) throws HygieiaException {
        return templateRepository.save(template);
    }

    @Override
    public Template update(Template template) throws HygieiaException {
        return templateRepository.save(template);
    }

    @Override
    public void delete(ObjectId id) {
        templateRepository.deleteById(id);
    }


    @Override
    public Template get(ObjectId id) {
        Template template = templateRepository.findById(id).get();
        return template;
    }

}
