import{y as a,j as r,U as e,l as c,F as o}from"./indexhtml-f0b6fea0.js";const t={selector:"vaadin-checkbox",displayName:"Checkbox",properties:[{propertyName:"--vaadin-checkbox-size",displayName:"Checkbox size",defaultValue:"var(--lumo-font-size-l)",editorType:a.range,presets:r.lumoFontSize,icon:"square"}]},l={selector:"vaadin-checkbox::part(checkbox)",displayName:"Checkmark box",properties:[e.backgroundColor,e.borderColor,e.borderWidth,e.borderRadius]},s={selector:"vaadin-checkbox[checked]::part(checkbox)",stateAttribute:"checked",displayName:"Checkmark box (when checked)",properties:[e.backgroundColor,e.borderColor,e.borderWidth,e.borderRadius]},i={selector:"vaadin-checkbox::part(checkbox)::after",displayName:"Checkmark",properties:[c.iconColor]},d={selector:"vaadin-checkbox label",displayName:"Label",properties:[o.textColor,o.fontSize,o.fontWeight,o.fontStyle]},k={tagName:"vaadin-checkbox",displayName:"Checkbox",elements:[t,l,s,i,d]};export{l as checkboxElement,s as checkedCheckboxElement,i as checkmarkElement,k as default,t as hostElement,d as labelElement};
