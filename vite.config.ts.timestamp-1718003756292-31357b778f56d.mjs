// vite.generated.ts
import path from "path";
import { existsSync as existsSync5, mkdirSync as mkdirSync2, readdirSync as readdirSync2, readFileSync as readFileSync4, writeFileSync as writeFileSync2 } from "fs";
import { createHash } from "crypto";
import * as net from "net";

// target/plugins/application-theme-plugin/theme-handle.js
import { existsSync as existsSync3, readFileSync as readFileSync2 } from "fs";
import { resolve as resolve3 } from "path";

// target/plugins/application-theme-plugin/theme-generator.js
import { globSync as globSync2 } from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/glob/dist/mjs/index.js";
import { resolve as resolve2, basename as basename2 } from "path";
import { existsSync as existsSync2, readFileSync, writeFileSync } from "fs";

// target/plugins/application-theme-plugin/theme-copy.js
import { readdirSync, statSync, mkdirSync, existsSync, copyFileSync } from "fs";
import { resolve, basename, relative, extname } from "path";
import { globSync } from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/glob/dist/mjs/index.js";
var ignoredFileExtensions = [".css", ".js", ".json"];
function copyThemeResources(themeFolder2, projectStaticAssetsOutputFolder, logger) {
  const staticAssetsThemeFolder = resolve(projectStaticAssetsOutputFolder, "themes", basename(themeFolder2));
  const collection = collectFolders(themeFolder2, logger);
  if (collection.files.length > 0) {
    mkdirSync(staticAssetsThemeFolder, { recursive: true });
    collection.directories.forEach((directory) => {
      const relativeDirectory = relative(themeFolder2, directory);
      const targetDirectory = resolve(staticAssetsThemeFolder, relativeDirectory);
      mkdirSync(targetDirectory, { recursive: true });
    });
    collection.files.forEach((file) => {
      const relativeFile = relative(themeFolder2, file);
      const targetFile = resolve(staticAssetsThemeFolder, relativeFile);
      copyFileIfAbsentOrNewer(file, targetFile, logger);
    });
  }
}
function collectFolders(folderToCopy, logger) {
  const collection = { directories: [], files: [] };
  logger.trace("files in directory", readdirSync(folderToCopy));
  readdirSync(folderToCopy).forEach((file) => {
    const fileToCopy = resolve(folderToCopy, file);
    try {
      if (statSync(fileToCopy).isDirectory()) {
        logger.debug("Going through directory", fileToCopy);
        const result = collectFolders(fileToCopy, logger);
        if (result.files.length > 0) {
          collection.directories.push(fileToCopy);
          logger.debug("Adding directory", fileToCopy);
          collection.directories.push.apply(collection.directories, result.directories);
          collection.files.push.apply(collection.files, result.files);
        }
      } else if (!ignoredFileExtensions.includes(extname(fileToCopy))) {
        logger.debug("Adding file", fileToCopy);
        collection.files.push(fileToCopy);
      }
    } catch (error) {
      handleNoSuchFileError(fileToCopy, error, logger);
    }
  });
  return collection;
}
function copyStaticAssets(themeName, themeProperties, projectStaticAssetsOutputFolder, logger) {
  const assets = themeProperties["assets"];
  if (!assets) {
    logger.debug("no assets to handle no static assets were copied");
    return;
  }
  mkdirSync(projectStaticAssetsOutputFolder, {
    recursive: true
  });
  const missingModules = checkModules(Object.keys(assets));
  if (missingModules.length > 0) {
    throw Error(
      "Missing npm modules '" + missingModules.join("', '") + "' for assets marked in 'theme.json'.\nInstall package(s) by adding a @NpmPackage annotation or install it using 'npm/pnpm i'"
    );
  }
  Object.keys(assets).forEach((module) => {
    const copyRules = assets[module];
    Object.keys(copyRules).forEach((copyRule) => {
      const nodeSources = resolve("node_modules/", module, copyRule);
      const files = globSync(nodeSources, { nodir: true });
      const targetFolder = resolve(projectStaticAssetsOutputFolder, "themes", themeName, copyRules[copyRule]);
      mkdirSync(targetFolder, {
        recursive: true
      });
      files.forEach((file) => {
        const copyTarget = resolve(targetFolder, basename(file));
        copyFileIfAbsentOrNewer(file, copyTarget, logger);
      });
    });
  });
}
function checkModules(modules) {
  const missing = [];
  modules.forEach((module) => {
    if (!existsSync(resolve("node_modules/", module))) {
      missing.push(module);
    }
  });
  return missing;
}
function copyFileIfAbsentOrNewer(fileToCopy, copyTarget, logger) {
  try {
    if (!existsSync(copyTarget) || statSync(copyTarget).mtime < statSync(fileToCopy).mtime) {
      logger.trace("Copying: ", fileToCopy, "=>", copyTarget);
      copyFileSync(fileToCopy, copyTarget);
    }
  } catch (error) {
    handleNoSuchFileError(fileToCopy, error, logger);
  }
}
function handleNoSuchFileError(file, error, logger) {
  if (error.code === "ENOENT") {
    logger.warn("Ignoring not existing file " + file + ". File may have been deleted during theme processing.");
  } else {
    throw error;
  }
}

// target/plugins/application-theme-plugin/theme-generator.js
var themeComponentsFolder = "components";
var documentCssFilename = "document.css";
var stylesCssFilename = "styles.css";
var CSSIMPORT_COMMENT = "CSSImport end";
var headerImport = `import 'construct-style-sheets-polyfill';
`;
function writeThemeFiles(themeFolder2, themeName, themeProperties, options) {
  const productionMode = !options.devMode;
  const useDevServerOrInProductionMode = !options.useDevBundle;
  const outputFolder = options.frontendGeneratedFolder;
  const styles = resolve2(themeFolder2, stylesCssFilename);
  const documentCssFile = resolve2(themeFolder2, documentCssFilename);
  const autoInjectComponents = themeProperties.autoInjectComponents ?? true;
  const globalFilename = "theme-" + themeName + ".global.generated.js";
  const componentsFilename = "theme-" + themeName + ".components.generated.js";
  const themeFilename = "theme-" + themeName + ".generated.js";
  let themeFileContent = headerImport;
  let globalImportContent = "// When this file is imported, global styles are automatically applied\n";
  let componentsFileContent = "";
  var componentsFiles;
  if (autoInjectComponents) {
    componentsFiles = globSync2("*.css", {
      cwd: resolve2(themeFolder2, themeComponentsFolder),
      nodir: true
    });
    if (componentsFiles.length > 0) {
      componentsFileContent += "import { unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin/register-styles';\n";
    }
  }
  if (themeProperties.parent) {
    themeFileContent += `import { applyTheme as applyBaseTheme } from './theme-${themeProperties.parent}.generated.js';
`;
  }
  themeFileContent += `import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';
`;
  themeFileContent += `import './${componentsFilename}';
`;
  themeFileContent += `let needsReloadOnChanges = false;
`;
  const imports = [];
  const componentCssImports = [];
  const globalFileContent = [];
  const globalCssCode = [];
  const shadowOnlyCss = [];
  const componentCssCode = [];
  const parentTheme = themeProperties.parent ? "applyBaseTheme(target);\n" : "";
  const parentThemeGlobalImport = themeProperties.parent ? `import './theme-${themeProperties.parent}.global.generated.js';
` : "";
  const themeIdentifier = "_vaadintheme_" + themeName + "_";
  const lumoCssFlag = "_vaadinthemelumoimports_";
  const globalCssFlag = themeIdentifier + "globalCss";
  const componentCssFlag = themeIdentifier + "componentCss";
  if (!existsSync2(styles)) {
    if (productionMode) {
      throw new Error(`styles.css file is missing and is needed for '${themeName}' in folder '${themeFolder2}'`);
    }
    writeFileSync(
      styles,
      "/* Import your application global css files here or add the styles directly to this file */",
      "utf8"
    );
  }
  let filename = basename2(styles);
  let variable = camelCase(filename);
  const lumoImports = themeProperties.lumoImports || ["color", "typography"];
  if (lumoImports) {
    lumoImports.forEach((lumoImport) => {
      imports.push(`import { ${lumoImport} } from '@vaadin/vaadin-lumo-styles/${lumoImport}.js';
`);
      if (lumoImport === "utility" || lumoImport === "badge" || lumoImport === "typography" || lumoImport === "color") {
        imports.push(`import '@vaadin/vaadin-lumo-styles/${lumoImport}-global.js';
`);
      }
    });
    lumoImports.forEach((lumoImport) => {
      shadowOnlyCss.push(`removers.push(injectGlobalCss(${lumoImport}.cssText, '', target, true));
`);
    });
  }
  if (useDevServerOrInProductionMode) {
    globalFileContent.push(parentThemeGlobalImport);
    globalFileContent.push(`import 'themes/${themeName}/${filename}';
`);
    imports.push(`import ${variable} from 'themes/${themeName}/${filename}?inline';
`);
    shadowOnlyCss.push(`removers.push(injectGlobalCss(${variable}.toString(), '', target));
    `);
  }
  if (existsSync2(documentCssFile)) {
    filename = basename2(documentCssFile);
    variable = camelCase(filename);
    if (useDevServerOrInProductionMode) {
      globalFileContent.push(`import 'themes/${themeName}/${filename}';
`);
      imports.push(`import ${variable} from 'themes/${themeName}/${filename}?inline';
`);
      shadowOnlyCss.push(`removers.push(injectGlobalCss(${variable}.toString(),'', document));
    `);
    }
  }
  let i = 0;
  if (themeProperties.documentCss) {
    const missingModules = checkModules(themeProperties.documentCss);
    if (missingModules.length > 0) {
      throw Error(
        "Missing npm modules or files '" + missingModules.join("', '") + "' for documentCss marked in 'theme.json'.\nInstall or update package(s) by adding a @NpmPackage annotation or install it using 'npm/pnpm i'"
      );
    }
    themeProperties.documentCss.forEach((cssImport) => {
      const variable2 = "module" + i++;
      imports.push(`import ${variable2} from '${cssImport}?inline';
`);
      globalCssCode.push(`if(target !== document) {
        removers.push(injectGlobalCss(${variable2}.toString(), '', target));
    }
    `);
      globalCssCode.push(
        `removers.push(injectGlobalCss(${variable2}.toString(), '${CSSIMPORT_COMMENT}', document));
    `
      );
    });
  }
  if (themeProperties.importCss) {
    const missingModules = checkModules(themeProperties.importCss);
    if (missingModules.length > 0) {
      throw Error(
        "Missing npm modules or files '" + missingModules.join("', '") + "' for importCss marked in 'theme.json'.\nInstall or update package(s) by adding a @NpmPackage annotation or install it using 'npm/pnpm i'"
      );
    }
    themeProperties.importCss.forEach((cssPath) => {
      const variable2 = "module" + i++;
      globalFileContent.push(`import '${cssPath}';
`);
      imports.push(`import ${variable2} from '${cssPath}?inline';
`);
      shadowOnlyCss.push(`removers.push(injectGlobalCss(${variable2}.toString(), '${CSSIMPORT_COMMENT}', target));
`);
    });
  }
  if (autoInjectComponents) {
    componentsFiles.forEach((componentCss) => {
      const filename2 = basename2(componentCss);
      const tag = filename2.replace(".css", "");
      const variable2 = camelCase(filename2);
      componentCssImports.push(
        `import ${variable2} from 'themes/${themeName}/${themeComponentsFolder}/${filename2}?inline';
`
      );
      const componentString = `registerStyles(
        '${tag}',
        unsafeCSS(${variable2}.toString())
      );
      `;
      componentCssCode.push(componentString);
    });
  }
  themeFileContent += imports.join("");
  const themeFileApply = `
  let themeRemovers = new WeakMap();
  let targets = [];

  export const applyTheme = (target) => {
    const removers = [];
    if (target !== document) {
      ${shadowOnlyCss.join("")}
    }
    ${parentTheme}
    ${globalCssCode.join("")}

    if (import.meta.hot) {
      targets.push(new WeakRef(target));
      themeRemovers.set(target, removers);
    }

  }
  
`;
  componentsFileContent += `
${componentCssImports.join("")}

if (!document['${componentCssFlag}']) {
  ${componentCssCode.join("")}
  document['${componentCssFlag}'] = true;
}

if (import.meta.hot) {
  import.meta.hot.accept((module) => {
    window.location.reload();
  });
}

`;
  themeFileContent += themeFileApply;
  themeFileContent += `
if (import.meta.hot) {
  import.meta.hot.accept((module) => {

    if (needsReloadOnChanges) {
      window.location.reload();
    } else {
      targets.forEach(targetRef => {
        const target = targetRef.deref();
        if (target) {
          themeRemovers.get(target).forEach(remover => remover())
          module.applyTheme(target);
        }
      })
    }
  });

  import.meta.hot.on('vite:afterUpdate', (update) => {
    document.dispatchEvent(new CustomEvent('vaadin-theme-updated', { detail: update }));
  });
}

`;
  globalImportContent += `
${globalFileContent.join("")}
`;
  writeIfChanged(resolve2(outputFolder, globalFilename), globalImportContent);
  writeIfChanged(resolve2(outputFolder, themeFilename), themeFileContent);
  writeIfChanged(resolve2(outputFolder, componentsFilename), componentsFileContent);
}
function writeIfChanged(file, data) {
  if (!existsSync2(file) || readFileSync(file, { encoding: "utf-8" }) !== data) {
    writeFileSync(file, data);
  }
}
function camelCase(str) {
  return str.replace(/(?:^\w|[A-Z]|\b\w)/g, function(word, index) {
    return index === 0 ? word.toLowerCase() : word.toUpperCase();
  }).replace(/\s+/g, "").replace(/\.|\-/g, "");
}

// target/plugins/application-theme-plugin/theme-handle.js
var nameRegex = /theme-(.*)\.generated\.js/;
var prevThemeName = void 0;
var firstThemeName = void 0;
function processThemeResources(options, logger) {
  const themeName = extractThemeName(options.frontendGeneratedFolder);
  if (themeName) {
    if (!prevThemeName && !firstThemeName) {
      firstThemeName = themeName;
    } else if (prevThemeName && prevThemeName !== themeName && firstThemeName !== themeName || !prevThemeName && firstThemeName !== themeName) {
      const warning = `Attention: Active theme is switched to '${themeName}'.`;
      const description = `
      Note that adding new style sheet files to '/themes/${themeName}/components', 
      may not be taken into effect until the next application restart.
      Changes to already existing style sheet files are being reloaded as before.`;
      logger.warn("*******************************************************************");
      logger.warn(warning);
      logger.warn(description);
      logger.warn("*******************************************************************");
    }
    prevThemeName = themeName;
    findThemeFolderAndHandleTheme(themeName, options, logger);
  } else {
    prevThemeName = void 0;
    logger.debug("Skipping Vaadin application theme handling.");
    logger.trace("Most likely no @Theme annotation for application or only themeClass used.");
  }
}
function findThemeFolderAndHandleTheme(themeName, options, logger) {
  let themeFound = false;
  for (let i = 0; i < options.themeProjectFolders.length; i++) {
    const themeProjectFolder = options.themeProjectFolders[i];
    if (existsSync3(themeProjectFolder)) {
      logger.debug("Searching themes folder '" + themeProjectFolder + "' for theme '" + themeName + "'");
      const handled = handleThemes(themeName, themeProjectFolder, options, logger);
      if (handled) {
        if (themeFound) {
          throw new Error(
            "Found theme files in '" + themeProjectFolder + "' and '" + themeFound + "'. Theme should only be available in one folder"
          );
        }
        logger.debug("Found theme files from '" + themeProjectFolder + "'");
        themeFound = themeProjectFolder;
      }
    }
  }
  if (existsSync3(options.themeResourceFolder)) {
    if (themeFound && existsSync3(resolve3(options.themeResourceFolder, themeName))) {
      throw new Error(
        "Theme '" + themeName + `'should not exist inside a jar and in the project at the same time
Extending another theme is possible by adding { "parent": "my-parent-theme" } entry to the theme.json file inside your theme folder.`
      );
    }
    logger.debug(
      "Searching theme jar resource folder '" + options.themeResourceFolder + "' for theme '" + themeName + "'"
    );
    handleThemes(themeName, options.themeResourceFolder, options, logger);
    themeFound = true;
  }
  return themeFound;
}
function handleThemes(themeName, themesFolder, options, logger) {
  const themeFolder2 = resolve3(themesFolder, themeName);
  if (existsSync3(themeFolder2)) {
    logger.debug("Found theme ", themeName, " in folder ", themeFolder2);
    const themeProperties = getThemeProperties(themeFolder2);
    if (themeProperties.parent) {
      const found = findThemeFolderAndHandleTheme(themeProperties.parent, options, logger);
      if (!found) {
        throw new Error(
          "Could not locate files for defined parent theme '" + themeProperties.parent + "'.\nPlease verify that dependency is added or theme folder exists."
        );
      }
    }
    copyStaticAssets(themeName, themeProperties, options.projectStaticAssetsOutputFolder, logger);
    copyThemeResources(themeFolder2, options.projectStaticAssetsOutputFolder, logger);
    writeThemeFiles(themeFolder2, themeName, themeProperties, options);
    return true;
  }
  return false;
}
function getThemeProperties(themeFolder2) {
  const themePropertyFile = resolve3(themeFolder2, "theme.json");
  if (!existsSync3(themePropertyFile)) {
    return {};
  }
  const themePropertyFileAsString = readFileSync2(themePropertyFile);
  if (themePropertyFileAsString.length === 0) {
    return {};
  }
  return JSON.parse(themePropertyFileAsString);
}
function extractThemeName(frontendGeneratedFolder) {
  if (!frontendGeneratedFolder) {
    throw new Error(
      "Couldn't extract theme name from 'theme.js', because the path to folder containing this file is empty. Please set the a correct folder path in ApplicationThemePlugin constructor parameters."
    );
  }
  const generatedThemeFile = resolve3(frontendGeneratedFolder, "theme.js");
  if (existsSync3(generatedThemeFile)) {
    const themeName = nameRegex.exec(readFileSync2(generatedThemeFile, { encoding: "utf8" }))[1];
    if (!themeName) {
      throw new Error("Couldn't parse theme name from '" + generatedThemeFile + "'.");
    }
    return themeName;
  } else {
    return "";
  }
}

// target/plugins/theme-loader/theme-loader-utils.js
import { existsSync as existsSync4, readFileSync as readFileSync3 } from "fs";
import { resolve as resolve4, basename as basename3 } from "path";
import { globSync as globSync3 } from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/glob/dist/mjs/index.js";
var urlMatcher = /(url\(\s*)(\'|\")?(\.\/|\.\.\/)(\S*)(\2\s*\))/g;
function assetsContains(fileUrl, themeFolder2, logger) {
  const themeProperties = getThemeProperties2(themeFolder2);
  if (!themeProperties) {
    logger.debug("No theme properties found.");
    return false;
  }
  const assets = themeProperties["assets"];
  if (!assets) {
    logger.debug("No defined assets in theme properties");
    return false;
  }
  for (let module of Object.keys(assets)) {
    const copyRules = assets[module];
    for (let copyRule of Object.keys(copyRules)) {
      if (fileUrl.startsWith(copyRules[copyRule])) {
        const targetFile = fileUrl.replace(copyRules[copyRule], "");
        const files = globSync3(resolve4("node_modules/", module, copyRule), { nodir: true });
        for (let file of files) {
          if (file.endsWith(targetFile))
            return true;
        }
      }
    }
  }
  return false;
}
function getThemeProperties2(themeFolder2) {
  const themePropertyFile = resolve4(themeFolder2, "theme.json");
  if (!existsSync4(themePropertyFile)) {
    return {};
  }
  const themePropertyFileAsString = readFileSync3(themePropertyFile);
  if (themePropertyFileAsString.length === 0) {
    return {};
  }
  return JSON.parse(themePropertyFileAsString);
}
function rewriteCssUrls(source, handledResourceFolder, themeFolder2, logger, options) {
  source = source.replace(urlMatcher, function(match, url, quoteMark, replace2, fileUrl, endString) {
    let absolutePath = resolve4(handledResourceFolder, replace2, fileUrl);
    const existingThemeResource = absolutePath.startsWith(themeFolder2) && existsSync4(absolutePath);
    if (existingThemeResource || assetsContains(fileUrl, themeFolder2, logger)) {
      const replacement = options.devMode ? "./" : "../static/";
      const skipLoader = existingThemeResource ? "" : replacement;
      const frontendThemeFolder = skipLoader + "themes/" + basename3(themeFolder2);
      logger.debug(
        "Updating url for file",
        "'" + replace2 + fileUrl + "'",
        "to use",
        "'" + frontendThemeFolder + "/" + fileUrl + "'"
      );
      const pathResolved = absolutePath.substring(themeFolder2.length).replace(/\\/g, "/");
      return url + (quoteMark ?? "") + frontendThemeFolder + pathResolved + endString;
    } else if (options.devMode) {
      logger.log("No rewrite for '", match, "' as the file was not found.");
    } else {
      return url + (quoteMark ?? "") + "../../" + fileUrl + endString;
    }
    return match;
  });
  return source;
}

// target/vaadin-dev-server-settings.json
var vaadin_dev_server_settings_default = {
  frontendFolder: "F:/Eclipse Workspace/megpbr/frontend",
  themeFolder: "themes",
  themeResourceFolder: "F:/Eclipse Workspace/megpbr/frontend/generated/jar-resources",
  staticOutput: "F:/Eclipse Workspace/megpbr/target/classes/META-INF/VAADIN/webapp/VAADIN/static",
  generatedFolder: "generated",
  statsOutput: "F:\\Eclipse Workspace\\megpbr\\target\\classes\\META-INF\\VAADIN\\config",
  frontendBundleOutput: "F:\\Eclipse Workspace\\megpbr\\target\\classes\\META-INF\\VAADIN\\webapp",
  devBundleOutput: "F:/Eclipse Workspace/megpbr/src/main/dev-bundle/webapp",
  devBundleStatsOutput: "F:/Eclipse Workspace/megpbr/src/main/dev-bundle/config",
  jarResourcesFolder: "F:/Eclipse Workspace/megpbr/frontend/generated/jar-resources",
  themeName: "megpbr",
  clientServiceWorkerSource: "F:\\Eclipse Workspace\\megpbr\\target\\sw.ts",
  pwaEnabled: true,
  offlineEnabled: true,
  offlinePath: "'.'"
};

// vite.generated.ts
import {
  defineConfig,
  mergeConfig
} from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/vite/dist/node/index.js";
import { getManifest } from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/workbox-build/build/index.js";
import * as rollup from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/rollup/dist/es/rollup.js";
import brotli from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/rollup-plugin-brotli/lib/index.cjs.js";
import replace from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/@rollup/plugin-replace/dist/es/index.js";
import checker from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/vite-plugin-checker/dist/esm/main.js";

// target/plugins/rollup-plugin-postcss-lit-custom/rollup-plugin-postcss-lit.js
import { createFilter } from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/@rollup/pluginutils/dist/es/index.js";
import transformAst from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/transform-ast/index.js";
var assetUrlRE = /__VITE_ASSET__([a-z\d]{8})__(?:\$_(.*?)__)?/g;
var escape = (str) => str.replace(assetUrlRE, '${unsafeCSSTag("__VITE_ASSET__$1__$2")}').replace(/`/g, "\\`").replace(/\\(?!`)/g, "\\\\");
function postcssLit(options = {}) {
  const defaultOptions = {
    include: "**/*.{css,sss,pcss,styl,stylus,sass,scss,less}",
    exclude: null,
    importPackage: "lit"
  };
  const opts = { ...defaultOptions, ...options };
  const filter = createFilter(opts.include, opts.exclude);
  return {
    name: "postcss-lit",
    enforce: "post",
    transform(code, id) {
      if (!filter(id))
        return;
      const ast = this.parse(code, {});
      let defaultExportName;
      let isDeclarationLiteral = false;
      const magicString = transformAst(code, { ast }, (node) => {
        if (node.type === "ExportDefaultDeclaration") {
          defaultExportName = node.declaration.name;
          isDeclarationLiteral = node.declaration.type === "Literal";
        }
      });
      if (!defaultExportName && !isDeclarationLiteral) {
        return;
      }
      magicString.walk((node) => {
        if (defaultExportName && node.type === "VariableDeclaration") {
          const exportedVar = node.declarations.find((d) => d.id.name === defaultExportName);
          if (exportedVar) {
            exportedVar.init.edit.update(`cssTag\`${escape(exportedVar.init.value)}\``);
          }
        }
        if (isDeclarationLiteral && node.type === "ExportDefaultDeclaration") {
          node.declaration.edit.update(`cssTag\`${escape(node.declaration.value)}\``);
        }
      });
      magicString.prepend(`import {css as cssTag, unsafeCSS as unsafeCSSTag} from '${opts.importPackage}';
`);
      return {
        code: magicString.toString(),
        map: magicString.generateMap({
          hires: true
        })
      };
    }
  };
}

// vite.generated.ts
import { createRequire } from "module";
import { visualizer } from "file:///F:/Eclipse%20Workspace/megpbr/node_modules/rollup-plugin-visualizer/dist/plugin/index.js";
var __vite_injected_original_dirname = "F:\\Eclipse Workspace\\megpbr";
var __vite_injected_original_import_meta_url = "file:///F:/Eclipse%20Workspace/megpbr/vite.generated.ts";
var require2 = createRequire(__vite_injected_original_import_meta_url);
var appShellUrl = ".";
var frontendFolder = path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.frontendFolder);
var themeFolder = path.resolve(frontendFolder, vaadin_dev_server_settings_default.themeFolder);
var frontendBundleFolder = path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.frontendBundleOutput);
var devBundleFolder = path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.devBundleOutput);
var devBundle = !!process.env.devBundle;
var jarResourcesFolder = path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.jarResourcesFolder);
var themeResourceFolder = path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.themeResourceFolder);
var projectPackageJsonFile = path.resolve(__vite_injected_original_dirname, "package.json");
var buildOutputFolder = devBundle ? devBundleFolder : frontendBundleFolder;
var statsFolder = path.resolve(__vite_injected_original_dirname, devBundle ? vaadin_dev_server_settings_default.devBundleStatsOutput : vaadin_dev_server_settings_default.statsOutput);
var statsFile = path.resolve(statsFolder, "stats.json");
var bundleSizeFile = path.resolve(statsFolder, "bundle-size.html");
var nodeModulesFolder = path.resolve(__vite_injected_original_dirname, "node_modules");
var webComponentTags = "";
var projectIndexHtml = path.resolve(frontendFolder, "index.html");
var projectStaticAssetsFolders = [
  path.resolve(__vite_injected_original_dirname, "src", "main", "resources", "META-INF", "resources"),
  path.resolve(__vite_injected_original_dirname, "src", "main", "resources", "static"),
  frontendFolder
];
var themeProjectFolders = projectStaticAssetsFolders.map((folder) => path.resolve(folder, vaadin_dev_server_settings_default.themeFolder));
var themeOptions = {
  devMode: false,
  useDevBundle: devBundle,
  // The following matches folder 'frontend/generated/themes/'
  // (not 'frontend/themes') for theme in JAR that is copied there
  themeResourceFolder: path.resolve(themeResourceFolder, vaadin_dev_server_settings_default.themeFolder),
  themeProjectFolders,
  projectStaticAssetsOutputFolder: devBundle ? path.resolve(devBundleFolder, "../assets") : path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.staticOutput),
  frontendGeneratedFolder: path.resolve(frontendFolder, vaadin_dev_server_settings_default.generatedFolder)
};
var hasExportedWebComponents = existsSync5(path.resolve(frontendFolder, "web-component.html"));
console.trace = () => {
};
console.debug = () => {
};
function injectManifestToSWPlugin() {
  const rewriteManifestIndexHtmlUrl = (manifest) => {
    const indexEntry = manifest.find((entry) => entry.url === "index.html");
    if (indexEntry) {
      indexEntry.url = appShellUrl;
    }
    return { manifest, warnings: [] };
  };
  return {
    name: "vaadin:inject-manifest-to-sw",
    async transform(code, id) {
      if (/sw\.(ts|js)$/.test(id)) {
        const { manifestEntries } = await getManifest({
          globDirectory: buildOutputFolder,
          globPatterns: ["**/*"],
          globIgnores: ["**/*.br"],
          manifestTransforms: [rewriteManifestIndexHtmlUrl],
          maximumFileSizeToCacheInBytes: 100 * 1024 * 1024
          // 100mb,
        });
        return code.replace("self.__WB_MANIFEST", JSON.stringify(manifestEntries));
      }
    }
  };
}
function buildSWPlugin(opts) {
  let config;
  const devMode = opts.devMode;
  const swObj = {};
  async function build(action, additionalPlugins = []) {
    const includedPluginNames = [
      "vite:esbuild",
      "rollup-plugin-dynamic-import-variables",
      "vite:esbuild-transpile",
      "vite:terser"
    ];
    const plugins = config.plugins.filter((p) => {
      return includedPluginNames.includes(p.name);
    });
    const resolver = config.createResolver();
    const resolvePlugin = {
      name: "resolver",
      resolveId(source, importer, _options) {
        return resolver(source, importer);
      }
    };
    plugins.unshift(resolvePlugin);
    plugins.push(
      replace({
        values: {
          "process.env.NODE_ENV": JSON.stringify(config.mode),
          ...config.define
        },
        preventAssignment: true
      })
    );
    if (additionalPlugins) {
      plugins.push(...additionalPlugins);
    }
    const bundle = await rollup.rollup({
      input: path.resolve(vaadin_dev_server_settings_default.clientServiceWorkerSource),
      plugins
    });
    try {
      return await bundle[action]({
        file: path.resolve(buildOutputFolder, "sw.js"),
        format: "es",
        exports: "none",
        sourcemap: config.command === "serve" || config.build.sourcemap,
        inlineDynamicImports: true
      });
    } finally {
      await bundle.close();
    }
  }
  return {
    name: "vaadin:build-sw",
    enforce: "post",
    async configResolved(resolvedConfig) {
      config = resolvedConfig;
    },
    async buildStart() {
      if (devMode) {
        const { output } = await build("generate");
        swObj.code = output[0].code;
        swObj.map = output[0].map;
      }
    },
    async load(id) {
      if (id.endsWith("sw.js")) {
        return "";
      }
    },
    async transform(_code, id) {
      if (id.endsWith("sw.js")) {
        return swObj;
      }
    },
    async closeBundle() {
      if (!devMode) {
        await build("write", [injectManifestToSWPlugin(), brotli()]);
      }
    }
  };
}
function statsExtracterPlugin() {
  function collectThemeJsonsInFrontend(themeJsonContents, themeName) {
    const themeJson = path.resolve(frontendFolder, vaadin_dev_server_settings_default.themeFolder, themeName, "theme.json");
    if (existsSync5(themeJson)) {
      const themeJsonContent = readFileSync4(themeJson, { encoding: "utf-8" }).replace(/\r\n/g, "\n");
      themeJsonContents[themeName] = themeJsonContent;
      const themeJsonObject = JSON.parse(themeJsonContent);
      if (themeJsonObject.parent) {
        collectThemeJsonsInFrontend(themeJsonContents, themeJsonObject.parent);
      }
    }
  }
  return {
    name: "vaadin:stats",
    enforce: "post",
    async writeBundle(options, bundle) {
      var _a;
      const modules = Object.values(bundle).flatMap((b) => b.modules ? Object.keys(b.modules) : []);
      const nodeModulesFolders = modules.map((id) => id.replace(/\\/g, "/")).filter((id) => id.startsWith(nodeModulesFolder.replace(/\\/g, "/"))).map((id) => id.substring(nodeModulesFolder.length + 1));
      const npmModules = nodeModulesFolders.map((id) => id.replace(/\\/g, "/")).map((id) => {
        const parts = id.split("/");
        if (id.startsWith("@")) {
          return parts[0] + "/" + parts[1];
        } else {
          return parts[0];
        }
      }).sort().filter((value, index, self) => self.indexOf(value) === index);
      const npmModuleAndVersion = Object.fromEntries(npmModules.map((module) => [module, getVersion(module)]));
      const cvdls = Object.fromEntries(
        npmModules.filter((module) => getCvdlName(module) != null).map((module) => [module, { name: getCvdlName(module), version: getVersion(module) }])
      );
      mkdirSync2(path.dirname(statsFile), { recursive: true });
      const projectPackageJson = JSON.parse(readFileSync4(projectPackageJsonFile, { encoding: "utf-8" }));
      const entryScripts = Object.values(bundle).filter((bundle2) => bundle2.isEntry).map((bundle2) => bundle2.fileName);
      const generatedIndexHtml = path.resolve(buildOutputFolder, "index.html");
      const customIndexData = readFileSync4(projectIndexHtml, { encoding: "utf-8" });
      const generatedIndexData = readFileSync4(generatedIndexHtml, {
        encoding: "utf-8"
      });
      const customIndexRows = new Set(customIndexData.split(/[\r\n]/).filter((row) => row.trim() !== ""));
      const generatedIndexRows = generatedIndexData.split(/[\r\n]/).filter((row) => row.trim() !== "");
      const rowsGenerated = [];
      generatedIndexRows.forEach((row) => {
        if (!customIndexRows.has(row)) {
          rowsGenerated.push(row);
        }
      });
      const parseImports = (filename, result) => {
        const content = readFileSync4(filename, { encoding: "utf-8" });
        const lines = content.split("\n");
        const staticImports = lines.filter((line) => line.startsWith("import ")).map((line) => line.substring(line.indexOf("'") + 1, line.lastIndexOf("'"))).map((line) => line.includes("?") ? line.substring(0, line.lastIndexOf("?")) : line);
        const dynamicImports = lines.filter((line) => line.includes("import(")).map((line) => line.replace(/.*import\(/, "")).map((line) => line.split(/'/)[1]).map((line) => line.includes("?") ? line.substring(0, line.lastIndexOf("?")) : line);
        staticImports.forEach((staticImport) => result.add(staticImport));
        dynamicImports.map((dynamicImport) => {
          const importedFile = path.resolve(path.dirname(filename), dynamicImport);
          parseImports(importedFile, result);
        });
      };
      const generatedImportsSet = /* @__PURE__ */ new Set();
      parseImports(
        path.resolve(themeOptions.frontendGeneratedFolder, "flow", "generated-flow-imports.js"),
        generatedImportsSet
      );
      const generatedImports = Array.from(generatedImportsSet).sort();
      const frontendFiles = {};
      const projectFileExtensions = [".js", ".js.map", ".ts", ".ts.map", ".tsx", ".tsx.map", ".css", ".css.map"];
      const isThemeComponentsResource = (id) => id.startsWith(themeOptions.frontendGeneratedFolder.replace(/\\/g, "/")) && id.match(/.*\/jar-resources\/themes\/[^\/]+\/components\//);
      modules.map((id) => id.replace(/\\/g, "/")).filter((id) => id.startsWith(frontendFolder.replace(/\\/g, "/"))).filter((id) => !id.startsWith(themeOptions.frontendGeneratedFolder.replace(/\\/g, "/")) || isThemeComponentsResource(id)).map((id) => id.substring(frontendFolder.length + 1)).map((line) => line.includes("?") ? line.substring(0, line.lastIndexOf("?")) : line).forEach((line) => {
        const filePath = path.resolve(frontendFolder, line);
        if (projectFileExtensions.includes(path.extname(filePath))) {
          const fileBuffer = readFileSync4(filePath, { encoding: "utf-8" }).replace(/\r\n/g, "\n");
          frontendFiles[line] = createHash("sha256").update(fileBuffer, "utf8").digest("hex");
        }
      });
      generatedImports.filter((line) => line.includes("generated/jar-resources")).forEach((line) => {
        let filename = line.substring(line.indexOf("generated"));
        const fileBuffer = readFileSync4(path.resolve(frontendFolder, filename), { encoding: "utf-8" }).replace(
          /\r\n/g,
          "\n"
        );
        const hash = createHash("sha256").update(fileBuffer, "utf8").digest("hex");
        const fileKey = line.substring(line.indexOf("jar-resources/") + 14);
        frontendFiles[fileKey] = hash;
      });
      if (existsSync5(path.resolve(frontendFolder, "index.ts"))) {
        const fileBuffer = readFileSync4(path.resolve(frontendFolder, "index.ts"), { encoding: "utf-8" }).replace(
          /\r\n/g,
          "\n"
        );
        frontendFiles[`index.ts`] = createHash("sha256").update(fileBuffer, "utf8").digest("hex");
      }
      const themeJsonContents = {};
      const themesFolder = path.resolve(jarResourcesFolder, "themes");
      if (existsSync5(themesFolder)) {
        readdirSync2(themesFolder).forEach((themeFolder2) => {
          const themeJson = path.resolve(themesFolder, themeFolder2, "theme.json");
          if (existsSync5(themeJson)) {
            themeJsonContents[path.basename(themeFolder2)] = readFileSync4(themeJson, { encoding: "utf-8" }).replace(
              /\r\n/g,
              "\n"
            );
          }
        });
      }
      collectThemeJsonsInFrontend(themeJsonContents, vaadin_dev_server_settings_default.themeName);
      let webComponents = [];
      if (webComponentTags) {
        webComponents = webComponentTags.split(";");
      }
      const stats = {
        packageJsonDependencies: projectPackageJson.dependencies,
        npmModules: npmModuleAndVersion,
        bundleImports: generatedImports,
        frontendHashes: frontendFiles,
        themeJsonContents,
        entryScripts,
        webComponents,
        cvdlModules: cvdls,
        packageJsonHash: (_a = projectPackageJson == null ? void 0 : projectPackageJson.vaadin) == null ? void 0 : _a.hash,
        indexHtmlGenerated: rowsGenerated
      };
      writeFileSync2(statsFile, JSON.stringify(stats, null, 1));
    }
  };
}
function vaadinBundlesPlugin() {
  const disabledMessage = "Vaadin component dependency bundles are disabled.";
  const modulesDirectory = nodeModulesFolder.replace(/\\/g, "/");
  let vaadinBundleJson;
  function parseModuleId(id) {
    const [scope, scopedPackageName] = id.split("/", 3);
    const packageName = scope.startsWith("@") ? `${scope}/${scopedPackageName}` : scope;
    const modulePath = `.${id.substring(packageName.length)}`;
    return {
      packageName,
      modulePath
    };
  }
  function getExports(id) {
    const { packageName, modulePath } = parseModuleId(id);
    const packageInfo = vaadinBundleJson.packages[packageName];
    if (!packageInfo)
      return;
    const exposeInfo = packageInfo.exposes[modulePath];
    if (!exposeInfo)
      return;
    const exportsSet = /* @__PURE__ */ new Set();
    for (const e of exposeInfo.exports) {
      if (typeof e === "string") {
        exportsSet.add(e);
      } else {
        const { namespace, source } = e;
        if (namespace) {
          exportsSet.add(namespace);
        } else {
          const sourceExports = getExports(source);
          if (sourceExports) {
            sourceExports.forEach((e2) => exportsSet.add(e2));
          }
        }
      }
    }
    return Array.from(exportsSet);
  }
  function getExportBinding(binding) {
    return binding === "default" ? "_default as default" : binding;
  }
  function getImportAssigment(binding) {
    return binding === "default" ? "default: _default" : binding;
  }
  return {
    name: "vaadin:bundles",
    enforce: "pre",
    apply(config, { command }) {
      if (command !== "serve")
        return false;
      try {
        const vaadinBundleJsonPath = require2.resolve("@vaadin/bundles/vaadin-bundle.json");
        vaadinBundleJson = JSON.parse(readFileSync4(vaadinBundleJsonPath, { encoding: "utf8" }));
      } catch (e) {
        if (typeof e === "object" && e.code === "MODULE_NOT_FOUND") {
          vaadinBundleJson = { packages: {} };
          console.info(`@vaadin/bundles npm package is not found, ${disabledMessage}`);
          return false;
        } else {
          throw e;
        }
      }
      const versionMismatches = [];
      for (const [name, packageInfo] of Object.entries(vaadinBundleJson.packages)) {
        let installedVersion = void 0;
        try {
          const { version: bundledVersion } = packageInfo;
          const installedPackageJsonFile = path.resolve(modulesDirectory, name, "package.json");
          const packageJson = JSON.parse(readFileSync4(installedPackageJsonFile, { encoding: "utf8" }));
          installedVersion = packageJson.version;
          if (installedVersion && installedVersion !== bundledVersion) {
            versionMismatches.push({
              name,
              bundledVersion,
              installedVersion
            });
          }
        } catch (_) {
        }
      }
      if (versionMismatches.length) {
        console.info(`@vaadin/bundles has version mismatches with installed packages, ${disabledMessage}`);
        console.info(`Packages with version mismatches: ${JSON.stringify(versionMismatches, void 0, 2)}`);
        vaadinBundleJson = { packages: {} };
        return false;
      }
      return true;
    },
    async config(config) {
      return mergeConfig(
        {
          optimizeDeps: {
            exclude: [
              // Vaadin bundle
              "@vaadin/bundles",
              ...Object.keys(vaadinBundleJson.packages),
              "@vaadin/vaadin-material-styles"
            ]
          }
        },
        config
      );
    },
    load(rawId) {
      const [path2, params] = rawId.split("?");
      if (!path2.startsWith(modulesDirectory))
        return;
      const id = path2.substring(modulesDirectory.length + 1);
      const bindings = getExports(id);
      if (bindings === void 0)
        return;
      const cacheSuffix = params ? `?${params}` : "";
      const bundlePath = `@vaadin/bundles/vaadin.js${cacheSuffix}`;
      return `import { init as VaadinBundleInit, get as VaadinBundleGet } from '${bundlePath}';
await VaadinBundleInit('default');
const { ${bindings.map(getImportAssigment).join(", ")} } = (await VaadinBundleGet('./node_modules/${id}'))();
export { ${bindings.map(getExportBinding).join(", ")} };`;
    }
  };
}
function themePlugin(opts) {
  const fullThemeOptions = { ...themeOptions, devMode: opts.devMode };
  return {
    name: "vaadin:theme",
    config() {
      processThemeResources(fullThemeOptions, console);
    },
    configureServer(server) {
      function handleThemeFileCreateDelete(themeFile, stats) {
        if (themeFile.startsWith(themeFolder)) {
          const changed = path.relative(themeFolder, themeFile);
          console.debug("Theme file " + (!!stats ? "created" : "deleted"), changed);
          processThemeResources(fullThemeOptions, console);
        }
      }
      server.watcher.on("add", handleThemeFileCreateDelete);
      server.watcher.on("unlink", handleThemeFileCreateDelete);
    },
    handleHotUpdate(context) {
      const contextPath = path.resolve(context.file);
      const themePath = path.resolve(themeFolder);
      if (contextPath.startsWith(themePath)) {
        const changed = path.relative(themePath, contextPath);
        console.debug("Theme file changed", changed);
        if (changed.startsWith(vaadin_dev_server_settings_default.themeName)) {
          processThemeResources(fullThemeOptions, console);
        }
      }
    },
    async resolveId(id, importer) {
      if (path.resolve(themeOptions.frontendGeneratedFolder, "theme.js") === importer && !existsSync5(path.resolve(themeOptions.frontendGeneratedFolder, id))) {
        console.debug("Generate theme file " + id + " not existing. Processing theme resource");
        processThemeResources(fullThemeOptions, console);
        return;
      }
      if (!id.startsWith(vaadin_dev_server_settings_default.themeFolder)) {
        return;
      }
      for (const location of [themeResourceFolder, frontendFolder]) {
        const result = await this.resolve(path.resolve(location, id));
        if (result) {
          return result;
        }
      }
    },
    async transform(raw, id, options) {
      const [bareId, query] = id.split("?");
      if (!(bareId == null ? void 0 : bareId.startsWith(themeFolder)) && !(bareId == null ? void 0 : bareId.startsWith(themeOptions.themeResourceFolder)) || !(bareId == null ? void 0 : bareId.endsWith(".css"))) {
        return;
      }
      const [themeName] = bareId.substring(themeFolder.length + 1).split("/");
      return rewriteCssUrls(raw, path.dirname(bareId), path.resolve(themeFolder, themeName), console, opts);
    }
  };
}
function runWatchDog(watchDogPort, watchDogHost) {
  const client = net.Socket();
  client.setEncoding("utf8");
  client.on("error", function(err) {
    console.log("Watchdog connection error. Terminating vite process...", err);
    client.destroy();
    process.exit(0);
  });
  client.on("close", function() {
    client.destroy();
    runWatchDog(watchDogPort, watchDogHost);
  });
  client.connect(watchDogPort, watchDogHost || "localhost");
}
var spaMiddlewareForceRemoved = false;
var allowedFrontendFolders = [frontendFolder, nodeModulesFolder];
function showRecompileReason() {
  return {
    name: "vaadin:why-you-compile",
    handleHotUpdate(context) {
      console.log("Recompiling because", context.file, "changed");
    }
  };
}
var DEV_MODE_START_REGEXP = /\/\*[\*!]\s+vaadin-dev-mode:start/;
var DEV_MODE_CODE_REGEXP = /\/\*[\*!]\s+vaadin-dev-mode:start([\s\S]*)vaadin-dev-mode:end\s+\*\*\//i;
function preserveUsageStats() {
  return {
    name: "vaadin:preserve-usage-stats",
    transform(src, id) {
      if (id.includes("vaadin-usage-statistics")) {
        if (src.includes("vaadin-dev-mode:start")) {
          const newSrc = src.replace(DEV_MODE_START_REGEXP, "/*! vaadin-dev-mode:start");
          if (newSrc === src) {
            console.error("Comment replacement failed to change anything");
          } else if (!newSrc.match(DEV_MODE_CODE_REGEXP)) {
            console.error("New comment fails to match original regexp");
          } else {
            return { code: newSrc };
          }
        }
      }
      return { code: src };
    }
  };
}
var vaadinConfig = (env) => {
  const devMode = env.mode === "development";
  const productionMode = !devMode && !devBundle;
  if (devMode && process.env.watchDogPort) {
    runWatchDog(process.env.watchDogPort, process.env.watchDogHost);
  }
  return {
    root: frontendFolder,
    base: "",
    publicDir: false,
    resolve: {
      alias: {
        "@vaadin/flow-frontend": jarResourcesFolder,
        Frontend: frontendFolder
      },
      preserveSymlinks: true
    },
    define: {
      OFFLINE_PATH: vaadin_dev_server_settings_default.offlinePath,
      VITE_ENABLED: "true"
    },
    server: {
      host: "127.0.0.1",
      strictPort: true,
      fs: {
        allow: allowedFrontendFolders
      }
    },
    build: {
      outDir: buildOutputFolder,
      emptyOutDir: devBundle,
      assetsDir: "VAADIN/build",
      rollupOptions: {
        input: {
          indexhtml: projectIndexHtml,
          ...hasExportedWebComponents ? { webcomponenthtml: path.resolve(frontendFolder, "web-component.html") } : {}
        },
        onwarn: (warning, defaultHandler) => {
          const ignoreEvalWarning = [
            "generated/jar-resources/FlowClient.js",
            "generated/jar-resources/vaadin-spreadsheet/spreadsheet-export.js",
            "@vaadin/charts/src/helpers.js"
          ];
          if (warning.code === "EVAL" && warning.id && !!ignoreEvalWarning.find((id) => warning.id.endsWith(id))) {
            return;
          }
          defaultHandler(warning);
        }
      }
    },
    optimizeDeps: {
      entries: [
        // Pre-scan entrypoints in Vite to avoid reloading on first open
        "generated/vaadin.ts"
      ],
      exclude: [
        "@vaadin/router",
        "@vaadin/vaadin-license-checker",
        "@vaadin/vaadin-usage-statistics",
        "workbox-core",
        "workbox-precaching",
        "workbox-routing",
        "workbox-strategies"
      ]
    },
    plugins: [
      productionMode && brotli(),
      devMode && vaadinBundlesPlugin(),
      devMode && showRecompileReason(),
      vaadin_dev_server_settings_default.offlineEnabled && buildSWPlugin({ devMode }),
      !devMode && statsExtracterPlugin(),
      devBundle && preserveUsageStats(),
      themePlugin({ devMode }),
      postcssLit({
        include: ["**/*.css", /.*\/.*\.css\?.*/],
        exclude: [
          `${themeFolder}/**/*.css`,
          new RegExp(`${themeFolder}/.*/.*\\.css\\?.*`),
          `${themeResourceFolder}/**/*.css`,
          new RegExp(`${themeResourceFolder}/.*/.*\\.css\\?.*`),
          new RegExp(".*/.*\\?html-proxy.*")
        ]
      }),
      {
        name: "vaadin:force-remove-html-middleware",
        transformIndexHtml: {
          enforce: "pre",
          transform(_html, { server }) {
            if (server && !spaMiddlewareForceRemoved) {
              server.middlewares.stack = server.middlewares.stack.filter((mw) => {
                const handleName = "" + mw.handle;
                return !handleName.includes("viteHtmlFallbackMiddleware");
              });
              spaMiddlewareForceRemoved = true;
            }
          }
        }
      },
      hasExportedWebComponents && {
        name: "vaadin:inject-entrypoints-to-web-component-html",
        transformIndexHtml: {
          enforce: "pre",
          transform(_html, { path: path2, server }) {
            if (path2 !== "/web-component.html") {
              return;
            }
            return [
              {
                tag: "script",
                attrs: { type: "module", src: `/generated/vaadin-web-component.ts` },
                injectTo: "head"
              }
            ];
          }
        }
      },
      {
        name: "vaadin:inject-entrypoints-to-index-html",
        transformIndexHtml: {
          enforce: "pre",
          transform(_html, { path: path2, server }) {
            if (path2 !== "/index.html") {
              return;
            }
            const scripts = [];
            if (devMode) {
              scripts.push({
                tag: "script",
                attrs: { type: "module", src: `/generated/vite-devmode.ts` },
                injectTo: "head"
              });
            }
            scripts.push({
              tag: "script",
              attrs: { type: "module", src: "/generated/vaadin.ts" },
              injectTo: "head"
            });
            return scripts;
          }
        }
      },
      checker({
        typescript: true
      }),
      productionMode && visualizer({ brotliSize: true, filename: bundleSizeFile })
    ]
  };
};
var overrideVaadinConfig = (customConfig2) => {
  return defineConfig((env) => mergeConfig(vaadinConfig(env), customConfig2(env)));
};
function getVersion(module) {
  const packageJson = path.resolve(nodeModulesFolder, module, "package.json");
  return JSON.parse(readFileSync4(packageJson, { encoding: "utf-8" })).version;
}
function getCvdlName(module) {
  const packageJson = path.resolve(nodeModulesFolder, module, "package.json");
  return JSON.parse(readFileSync4(packageJson, { encoding: "utf-8" })).cvdlName;
}

// vite.config.ts
var customConfig = (env) => ({
  // Here you can add custom Vite parameters
  // https://vitejs.dev/config/
});
var vite_config_default = overrideVaadinConfig(customConfig);
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5nZW5lcmF0ZWQudHMiLCAidGFyZ2V0L3BsdWdpbnMvYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luL3RoZW1lLWhhbmRsZS5qcyIsICJ0YXJnZXQvcGx1Z2lucy9hcHBsaWNhdGlvbi10aGVtZS1wbHVnaW4vdGhlbWUtZ2VuZXJhdG9yLmpzIiwgInRhcmdldC9wbHVnaW5zL2FwcGxpY2F0aW9uLXRoZW1lLXBsdWdpbi90aGVtZS1jb3B5LmpzIiwgInRhcmdldC9wbHVnaW5zL3RoZW1lLWxvYWRlci90aGVtZS1sb2FkZXItdXRpbHMuanMiLCAidGFyZ2V0L3ZhYWRpbi1kZXYtc2VydmVyLXNldHRpbmdzLmpzb24iLCAidGFyZ2V0L3BsdWdpbnMvcm9sbHVwLXBsdWdpbi1wb3N0Y3NzLWxpdC1jdXN0b20vcm9sbHVwLXBsdWdpbi1wb3N0Y3NzLWxpdC5qcyIsICJ2aXRlLmNvbmZpZy50cyJdLAogICJzb3VyY2VzQ29udGVudCI6IFsiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkY6XFxcXEVjbGlwc2UgV29ya3NwYWNlXFxcXG1lZ3BiclwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiRjpcXFxcRWNsaXBzZSBXb3Jrc3BhY2VcXFxcbWVncGJyXFxcXHZpdGUuZ2VuZXJhdGVkLnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9GOi9FY2xpcHNlJTIwV29ya3NwYWNlL21lZ3Bici92aXRlLmdlbmVyYXRlZC50c1wiOy8qKlxuICogTk9USUNFOiB0aGlzIGlzIGFuIGF1dG8tZ2VuZXJhdGVkIGZpbGVcbiAqXG4gKiBUaGlzIGZpbGUgaGFzIGJlZW4gZ2VuZXJhdGVkIGJ5IHRoZSBgZmxvdzpwcmVwYXJlLWZyb250ZW5kYCBtYXZlbiBnb2FsLlxuICogVGhpcyBmaWxlIHdpbGwgYmUgb3ZlcndyaXR0ZW4gb24gZXZlcnkgcnVuLiBBbnkgY3VzdG9tIGNoYW5nZXMgc2hvdWxkIGJlIG1hZGUgdG8gdml0ZS5jb25maWcudHNcbiAqL1xuaW1wb3J0IHBhdGggZnJvbSAncGF0aCc7XG5pbXBvcnQgeyBleGlzdHNTeW5jLCBta2RpclN5bmMsIHJlYWRkaXJTeW5jLCByZWFkRmlsZVN5bmMsIHdyaXRlRmlsZVN5bmMgfSBmcm9tICdmcyc7XG5pbXBvcnQgeyBjcmVhdGVIYXNoIH0gZnJvbSAnY3J5cHRvJztcbmltcG9ydCAqIGFzIG5ldCBmcm9tICduZXQnO1xuXG5pbXBvcnQgeyBwcm9jZXNzVGhlbWVSZXNvdXJjZXMgfSBmcm9tICcuL3RhcmdldC9wbHVnaW5zL2FwcGxpY2F0aW9uLXRoZW1lLXBsdWdpbi90aGVtZS1oYW5kbGUuanMnO1xuaW1wb3J0IHsgcmV3cml0ZUNzc1VybHMgfSBmcm9tICcuL3RhcmdldC9wbHVnaW5zL3RoZW1lLWxvYWRlci90aGVtZS1sb2FkZXItdXRpbHMuanMnO1xuaW1wb3J0IHNldHRpbmdzIGZyb20gJy4vdGFyZ2V0L3ZhYWRpbi1kZXYtc2VydmVyLXNldHRpbmdzLmpzb24nO1xuaW1wb3J0IHtcbiAgQXNzZXRJbmZvLFxuICBDaHVua0luZm8sXG4gIGRlZmluZUNvbmZpZyxcbiAgbWVyZ2VDb25maWcsXG4gIE91dHB1dE9wdGlvbnMsXG4gIFBsdWdpbk9wdGlvbixcbiAgUmVzb2x2ZWRDb25maWcsXG4gIFVzZXJDb25maWdGblxufSBmcm9tICd2aXRlJztcbmltcG9ydCB7IGdldE1hbmlmZXN0IH0gZnJvbSAnd29ya2JveC1idWlsZCc7XG5cbmltcG9ydCAqIGFzIHJvbGx1cCBmcm9tICdyb2xsdXAnO1xuaW1wb3J0IGJyb3RsaSBmcm9tICdyb2xsdXAtcGx1Z2luLWJyb3RsaSc7XG5pbXBvcnQgcmVwbGFjZSBmcm9tICdAcm9sbHVwL3BsdWdpbi1yZXBsYWNlJztcbmltcG9ydCBjaGVja2VyIGZyb20gJ3ZpdGUtcGx1Z2luLWNoZWNrZXInO1xuaW1wb3J0IHBvc3Rjc3NMaXQgZnJvbSAnLi90YXJnZXQvcGx1Z2lucy9yb2xsdXAtcGx1Z2luLXBvc3Rjc3MtbGl0LWN1c3RvbS9yb2xsdXAtcGx1Z2luLXBvc3Rjc3MtbGl0LmpzJztcblxuaW1wb3J0IHsgY3JlYXRlUmVxdWlyZSB9IGZyb20gJ21vZHVsZSc7XG5cbmltcG9ydCB7IHZpc3VhbGl6ZXIgfSBmcm9tICdyb2xsdXAtcGx1Z2luLXZpc3VhbGl6ZXInO1xuXG4vLyBNYWtlIGByZXF1aXJlYCBjb21wYXRpYmxlIHdpdGggRVMgbW9kdWxlc1xuY29uc3QgcmVxdWlyZSA9IGNyZWF0ZVJlcXVpcmUoaW1wb3J0Lm1ldGEudXJsKTtcblxuY29uc3QgYXBwU2hlbGxVcmwgPSAnLic7XG5cbmNvbnN0IGZyb250ZW5kRm9sZGVyID0gcGF0aC5yZXNvbHZlKF9fZGlybmFtZSwgc2V0dGluZ3MuZnJvbnRlbmRGb2xkZXIpO1xuY29uc3QgdGhlbWVGb2xkZXIgPSBwYXRoLnJlc29sdmUoZnJvbnRlbmRGb2xkZXIsIHNldHRpbmdzLnRoZW1lRm9sZGVyKTtcbmNvbnN0IGZyb250ZW5kQnVuZGxlRm9sZGVyID0gcGF0aC5yZXNvbHZlKF9fZGlybmFtZSwgc2V0dGluZ3MuZnJvbnRlbmRCdW5kbGVPdXRwdXQpO1xuY29uc3QgZGV2QnVuZGxlRm9sZGVyID0gcGF0aC5yZXNvbHZlKF9fZGlybmFtZSwgc2V0dGluZ3MuZGV2QnVuZGxlT3V0cHV0KTtcbmNvbnN0IGRldkJ1bmRsZSA9ICEhcHJvY2Vzcy5lbnYuZGV2QnVuZGxlO1xuY29uc3QgamFyUmVzb3VyY2VzRm9sZGVyID0gcGF0aC5yZXNvbHZlKF9fZGlybmFtZSwgc2V0dGluZ3MuamFyUmVzb3VyY2VzRm9sZGVyKTtcbmNvbnN0IHRoZW1lUmVzb3VyY2VGb2xkZXIgPSBwYXRoLnJlc29sdmUoX19kaXJuYW1lLCBzZXR0aW5ncy50aGVtZVJlc291cmNlRm9sZGVyKTtcbmNvbnN0IHByb2plY3RQYWNrYWdlSnNvbkZpbGUgPSBwYXRoLnJlc29sdmUoX19kaXJuYW1lLCAncGFja2FnZS5qc29uJyk7XG5cbmNvbnN0IGJ1aWxkT3V0cHV0Rm9sZGVyID0gZGV2QnVuZGxlID8gZGV2QnVuZGxlRm9sZGVyIDogZnJvbnRlbmRCdW5kbGVGb2xkZXI7XG5jb25zdCBzdGF0c0ZvbGRlciA9IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsIGRldkJ1bmRsZSA/IHNldHRpbmdzLmRldkJ1bmRsZVN0YXRzT3V0cHV0IDogc2V0dGluZ3Muc3RhdHNPdXRwdXQpO1xuY29uc3Qgc3RhdHNGaWxlID0gcGF0aC5yZXNvbHZlKHN0YXRzRm9sZGVyLCAnc3RhdHMuanNvbicpO1xuY29uc3QgYnVuZGxlU2l6ZUZpbGUgPSBwYXRoLnJlc29sdmUoc3RhdHNGb2xkZXIsICdidW5kbGUtc2l6ZS5odG1sJyk7XG5jb25zdCBub2RlTW9kdWxlc0ZvbGRlciA9IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsICdub2RlX21vZHVsZXMnKTtcbmNvbnN0IHdlYkNvbXBvbmVudFRhZ3MgPSAnJztcblxuY29uc3QgcHJvamVjdEluZGV4SHRtbCA9IHBhdGgucmVzb2x2ZShmcm9udGVuZEZvbGRlciwgJ2luZGV4Lmh0bWwnKTtcblxuY29uc3QgcHJvamVjdFN0YXRpY0Fzc2V0c0ZvbGRlcnMgPSBbXG4gIHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsICdzcmMnLCAnbWFpbicsICdyZXNvdXJjZXMnLCAnTUVUQS1JTkYnLCAncmVzb3VyY2VzJyksXG4gIHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsICdzcmMnLCAnbWFpbicsICdyZXNvdXJjZXMnLCAnc3RhdGljJyksXG4gIGZyb250ZW5kRm9sZGVyXG5dO1xuXG4vLyBGb2xkZXJzIGluIHRoZSBwcm9qZWN0IHdoaWNoIGNhbiBjb250YWluIGFwcGxpY2F0aW9uIHRoZW1lc1xuY29uc3QgdGhlbWVQcm9qZWN0Rm9sZGVycyA9IHByb2plY3RTdGF0aWNBc3NldHNGb2xkZXJzLm1hcCgoZm9sZGVyKSA9PiBwYXRoLnJlc29sdmUoZm9sZGVyLCBzZXR0aW5ncy50aGVtZUZvbGRlcikpO1xuXG5jb25zdCB0aGVtZU9wdGlvbnMgPSB7XG4gIGRldk1vZGU6IGZhbHNlLFxuICB1c2VEZXZCdW5kbGU6IGRldkJ1bmRsZSxcbiAgLy8gVGhlIGZvbGxvd2luZyBtYXRjaGVzIGZvbGRlciAnZnJvbnRlbmQvZ2VuZXJhdGVkL3RoZW1lcy8nXG4gIC8vIChub3QgJ2Zyb250ZW5kL3RoZW1lcycpIGZvciB0aGVtZSBpbiBKQVIgdGhhdCBpcyBjb3BpZWQgdGhlcmVcbiAgdGhlbWVSZXNvdXJjZUZvbGRlcjogcGF0aC5yZXNvbHZlKHRoZW1lUmVzb3VyY2VGb2xkZXIsIHNldHRpbmdzLnRoZW1lRm9sZGVyKSxcbiAgdGhlbWVQcm9qZWN0Rm9sZGVyczogdGhlbWVQcm9qZWN0Rm9sZGVycyxcbiAgcHJvamVjdFN0YXRpY0Fzc2V0c091dHB1dEZvbGRlcjogZGV2QnVuZGxlXG4gICAgPyBwYXRoLnJlc29sdmUoZGV2QnVuZGxlRm9sZGVyLCAnLi4vYXNzZXRzJylcbiAgICA6IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsIHNldHRpbmdzLnN0YXRpY091dHB1dCksXG4gIGZyb250ZW5kR2VuZXJhdGVkRm9sZGVyOiBwYXRoLnJlc29sdmUoZnJvbnRlbmRGb2xkZXIsIHNldHRpbmdzLmdlbmVyYXRlZEZvbGRlcilcbn07XG5cbmNvbnN0IGhhc0V4cG9ydGVkV2ViQ29tcG9uZW50cyA9IGV4aXN0c1N5bmMocGF0aC5yZXNvbHZlKGZyb250ZW5kRm9sZGVyLCAnd2ViLWNvbXBvbmVudC5odG1sJykpO1xuXG4vLyBCbG9jayBkZWJ1ZyBhbmQgdHJhY2UgbG9ncy5cbmNvbnNvbGUudHJhY2UgPSAoKSA9PiB7fTtcbmNvbnNvbGUuZGVidWcgPSAoKSA9PiB7fTtcblxuZnVuY3Rpb24gaW5qZWN0TWFuaWZlc3RUb1NXUGx1Z2luKCk6IHJvbGx1cC5QbHVnaW4ge1xuICBjb25zdCByZXdyaXRlTWFuaWZlc3RJbmRleEh0bWxVcmwgPSAobWFuaWZlc3QpID0+IHtcbiAgICBjb25zdCBpbmRleEVudHJ5ID0gbWFuaWZlc3QuZmluZCgoZW50cnkpID0+IGVudHJ5LnVybCA9PT0gJ2luZGV4Lmh0bWwnKTtcbiAgICBpZiAoaW5kZXhFbnRyeSkge1xuICAgICAgaW5kZXhFbnRyeS51cmwgPSBhcHBTaGVsbFVybDtcbiAgICB9XG5cbiAgICByZXR1cm4geyBtYW5pZmVzdCwgd2FybmluZ3M6IFtdIH07XG4gIH07XG5cbiAgcmV0dXJuIHtcbiAgICBuYW1lOiAndmFhZGluOmluamVjdC1tYW5pZmVzdC10by1zdycsXG4gICAgYXN5bmMgdHJhbnNmb3JtKGNvZGUsIGlkKSB7XG4gICAgICBpZiAoL3N3XFwuKHRzfGpzKSQvLnRlc3QoaWQpKSB7XG4gICAgICAgIGNvbnN0IHsgbWFuaWZlc3RFbnRyaWVzIH0gPSBhd2FpdCBnZXRNYW5pZmVzdCh7XG4gICAgICAgICAgZ2xvYkRpcmVjdG9yeTogYnVpbGRPdXRwdXRGb2xkZXIsXG4gICAgICAgICAgZ2xvYlBhdHRlcm5zOiBbJyoqLyonXSxcbiAgICAgICAgICBnbG9iSWdub3JlczogWycqKi8qLmJyJ10sXG4gICAgICAgICAgbWFuaWZlc3RUcmFuc2Zvcm1zOiBbcmV3cml0ZU1hbmlmZXN0SW5kZXhIdG1sVXJsXSxcbiAgICAgICAgICBtYXhpbXVtRmlsZVNpemVUb0NhY2hlSW5CeXRlczogMTAwICogMTAyNCAqIDEwMjQgLy8gMTAwbWIsXG4gICAgICAgIH0pO1xuXG4gICAgICAgIHJldHVybiBjb2RlLnJlcGxhY2UoJ3NlbGYuX19XQl9NQU5JRkVTVCcsIEpTT04uc3RyaW5naWZ5KG1hbmlmZXN0RW50cmllcykpO1xuICAgICAgfVxuICAgIH1cbiAgfTtcbn1cblxuZnVuY3Rpb24gYnVpbGRTV1BsdWdpbihvcHRzKTogUGx1Z2luT3B0aW9uIHtcbiAgbGV0IGNvbmZpZzogUmVzb2x2ZWRDb25maWc7XG4gIGNvbnN0IGRldk1vZGUgPSBvcHRzLmRldk1vZGU7XG5cbiAgY29uc3Qgc3dPYmogPSB7fTtcblxuICBhc3luYyBmdW5jdGlvbiBidWlsZChhY3Rpb246ICdnZW5lcmF0ZScgfCAnd3JpdGUnLCBhZGRpdGlvbmFsUGx1Z2luczogcm9sbHVwLlBsdWdpbltdID0gW10pIHtcbiAgICBjb25zdCBpbmNsdWRlZFBsdWdpbk5hbWVzID0gW1xuICAgICAgJ3ZpdGU6ZXNidWlsZCcsXG4gICAgICAncm9sbHVwLXBsdWdpbi1keW5hbWljLWltcG9ydC12YXJpYWJsZXMnLFxuICAgICAgJ3ZpdGU6ZXNidWlsZC10cmFuc3BpbGUnLFxuICAgICAgJ3ZpdGU6dGVyc2VyJ1xuICAgIF07XG4gICAgY29uc3QgcGx1Z2luczogcm9sbHVwLlBsdWdpbltdID0gY29uZmlnLnBsdWdpbnMuZmlsdGVyKChwKSA9PiB7XG4gICAgICByZXR1cm4gaW5jbHVkZWRQbHVnaW5OYW1lcy5pbmNsdWRlcyhwLm5hbWUpO1xuICAgIH0pO1xuICAgIGNvbnN0IHJlc29sdmVyID0gY29uZmlnLmNyZWF0ZVJlc29sdmVyKCk7XG4gICAgY29uc3QgcmVzb2x2ZVBsdWdpbjogcm9sbHVwLlBsdWdpbiA9IHtcbiAgICAgIG5hbWU6ICdyZXNvbHZlcicsXG4gICAgICByZXNvbHZlSWQoc291cmNlLCBpbXBvcnRlciwgX29wdGlvbnMpIHtcbiAgICAgICAgcmV0dXJuIHJlc29sdmVyKHNvdXJjZSwgaW1wb3J0ZXIpO1xuICAgICAgfVxuICAgIH07XG4gICAgcGx1Z2lucy51bnNoaWZ0KHJlc29sdmVQbHVnaW4pOyAvLyBQdXQgcmVzb2x2ZSBmaXJzdFxuICAgIHBsdWdpbnMucHVzaChcbiAgICAgIHJlcGxhY2Uoe1xuICAgICAgICB2YWx1ZXM6IHtcbiAgICAgICAgICAncHJvY2Vzcy5lbnYuTk9ERV9FTlYnOiBKU09OLnN0cmluZ2lmeShjb25maWcubW9kZSksXG4gICAgICAgICAgLi4uY29uZmlnLmRlZmluZVxuICAgICAgICB9LFxuICAgICAgICBwcmV2ZW50QXNzaWdubWVudDogdHJ1ZVxuICAgICAgfSlcbiAgICApO1xuICAgIGlmIChhZGRpdGlvbmFsUGx1Z2lucykge1xuICAgICAgcGx1Z2lucy5wdXNoKC4uLmFkZGl0aW9uYWxQbHVnaW5zKTtcbiAgICB9XG4gICAgY29uc3QgYnVuZGxlID0gYXdhaXQgcm9sbHVwLnJvbGx1cCh7XG4gICAgICBpbnB1dDogcGF0aC5yZXNvbHZlKHNldHRpbmdzLmNsaWVudFNlcnZpY2VXb3JrZXJTb3VyY2UpLFxuICAgICAgcGx1Z2luc1xuICAgIH0pO1xuXG4gICAgdHJ5IHtcbiAgICAgIHJldHVybiBhd2FpdCBidW5kbGVbYWN0aW9uXSh7XG4gICAgICAgIGZpbGU6IHBhdGgucmVzb2x2ZShidWlsZE91dHB1dEZvbGRlciwgJ3N3LmpzJyksXG4gICAgICAgIGZvcm1hdDogJ2VzJyxcbiAgICAgICAgZXhwb3J0czogJ25vbmUnLFxuICAgICAgICBzb3VyY2VtYXA6IGNvbmZpZy5jb21tYW5kID09PSAnc2VydmUnIHx8IGNvbmZpZy5idWlsZC5zb3VyY2VtYXAsXG4gICAgICAgIGlubGluZUR5bmFtaWNJbXBvcnRzOiB0cnVlXG4gICAgICB9KTtcbiAgICB9IGZpbmFsbHkge1xuICAgICAgYXdhaXQgYnVuZGxlLmNsb3NlKCk7XG4gICAgfVxuICB9XG5cbiAgcmV0dXJuIHtcbiAgICBuYW1lOiAndmFhZGluOmJ1aWxkLXN3JyxcbiAgICBlbmZvcmNlOiAncG9zdCcsXG4gICAgYXN5bmMgY29uZmlnUmVzb2x2ZWQocmVzb2x2ZWRDb25maWcpIHtcbiAgICAgIGNvbmZpZyA9IHJlc29sdmVkQ29uZmlnO1xuICAgIH0sXG4gICAgYXN5bmMgYnVpbGRTdGFydCgpIHtcbiAgICAgIGlmIChkZXZNb2RlKSB7XG4gICAgICAgIGNvbnN0IHsgb3V0cHV0IH0gPSBhd2FpdCBidWlsZCgnZ2VuZXJhdGUnKTtcbiAgICAgICAgc3dPYmouY29kZSA9IG91dHB1dFswXS5jb2RlO1xuICAgICAgICBzd09iai5tYXAgPSBvdXRwdXRbMF0ubWFwO1xuICAgICAgfVxuICAgIH0sXG4gICAgYXN5bmMgbG9hZChpZCkge1xuICAgICAgaWYgKGlkLmVuZHNXaXRoKCdzdy5qcycpKSB7XG4gICAgICAgIHJldHVybiAnJztcbiAgICAgIH1cbiAgICB9LFxuICAgIGFzeW5jIHRyYW5zZm9ybShfY29kZSwgaWQpIHtcbiAgICAgIGlmIChpZC5lbmRzV2l0aCgnc3cuanMnKSkge1xuICAgICAgICByZXR1cm4gc3dPYmo7XG4gICAgICB9XG4gICAgfSxcbiAgICBhc3luYyBjbG9zZUJ1bmRsZSgpIHtcbiAgICAgIGlmICghZGV2TW9kZSkge1xuICAgICAgICBhd2FpdCBidWlsZCgnd3JpdGUnLCBbaW5qZWN0TWFuaWZlc3RUb1NXUGx1Z2luKCksIGJyb3RsaSgpXSk7XG4gICAgICB9XG4gICAgfVxuICB9O1xufVxuXG5mdW5jdGlvbiBzdGF0c0V4dHJhY3RlclBsdWdpbigpOiBQbHVnaW5PcHRpb24ge1xuICBmdW5jdGlvbiBjb2xsZWN0VGhlbWVKc29uc0luRnJvbnRlbmQodGhlbWVKc29uQ29udGVudHM6IFJlY29yZDxzdHJpbmcsIHN0cmluZz4sIHRoZW1lTmFtZTogc3RyaW5nKSB7XG4gICAgY29uc3QgdGhlbWVKc29uID0gcGF0aC5yZXNvbHZlKGZyb250ZW5kRm9sZGVyLCBzZXR0aW5ncy50aGVtZUZvbGRlciwgdGhlbWVOYW1lLCAndGhlbWUuanNvbicpO1xuICAgIGlmIChleGlzdHNTeW5jKHRoZW1lSnNvbikpIHtcbiAgICAgIGNvbnN0IHRoZW1lSnNvbkNvbnRlbnQgPSByZWFkRmlsZVN5bmModGhlbWVKc29uLCB7IGVuY29kaW5nOiAndXRmLTgnIH0pLnJlcGxhY2UoL1xcclxcbi9nLCAnXFxuJyk7XG4gICAgICB0aGVtZUpzb25Db250ZW50c1t0aGVtZU5hbWVdID0gdGhlbWVKc29uQ29udGVudDtcbiAgICAgIGNvbnN0IHRoZW1lSnNvbk9iamVjdCA9IEpTT04ucGFyc2UodGhlbWVKc29uQ29udGVudCk7XG4gICAgICBpZiAodGhlbWVKc29uT2JqZWN0LnBhcmVudCkge1xuICAgICAgICBjb2xsZWN0VGhlbWVKc29uc0luRnJvbnRlbmQodGhlbWVKc29uQ29udGVudHMsIHRoZW1lSnNvbk9iamVjdC5wYXJlbnQpO1xuICAgICAgfVxuICAgIH1cbiAgfVxuXG4gIHJldHVybiB7XG4gICAgbmFtZTogJ3ZhYWRpbjpzdGF0cycsXG4gICAgZW5mb3JjZTogJ3Bvc3QnLFxuICAgIGFzeW5jIHdyaXRlQnVuZGxlKG9wdGlvbnM6IE91dHB1dE9wdGlvbnMsIGJ1bmRsZTogeyBbZmlsZU5hbWU6IHN0cmluZ106IEFzc2V0SW5mbyB8IENodW5rSW5mbyB9KSB7XG4gICAgICBjb25zdCBtb2R1bGVzID0gT2JqZWN0LnZhbHVlcyhidW5kbGUpLmZsYXRNYXAoKGIpID0+IChiLm1vZHVsZXMgPyBPYmplY3Qua2V5cyhiLm1vZHVsZXMpIDogW10pKTtcbiAgICAgIGNvbnN0IG5vZGVNb2R1bGVzRm9sZGVycyA9IG1vZHVsZXNcbiAgICAgICAgLm1hcCgoaWQpID0+IGlkLnJlcGxhY2UoL1xcXFwvZywgJy8nKSlcbiAgICAgICAgLmZpbHRlcigoaWQpID0+IGlkLnN0YXJ0c1dpdGgobm9kZU1vZHVsZXNGb2xkZXIucmVwbGFjZSgvXFxcXC9nLCAnLycpKSlcbiAgICAgICAgLm1hcCgoaWQpID0+IGlkLnN1YnN0cmluZyhub2RlTW9kdWxlc0ZvbGRlci5sZW5ndGggKyAxKSk7XG4gICAgICBjb25zdCBucG1Nb2R1bGVzID0gbm9kZU1vZHVsZXNGb2xkZXJzXG4gICAgICAgIC5tYXAoKGlkKSA9PiBpZC5yZXBsYWNlKC9cXFxcL2csICcvJykpXG4gICAgICAgIC5tYXAoKGlkKSA9PiB7XG4gICAgICAgICAgY29uc3QgcGFydHMgPSBpZC5zcGxpdCgnLycpO1xuICAgICAgICAgIGlmIChpZC5zdGFydHNXaXRoKCdAJykpIHtcbiAgICAgICAgICAgIHJldHVybiBwYXJ0c1swXSArICcvJyArIHBhcnRzWzFdO1xuICAgICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgICByZXR1cm4gcGFydHNbMF07XG4gICAgICAgICAgfVxuICAgICAgICB9KVxuICAgICAgICAuc29ydCgpXG4gICAgICAgIC5maWx0ZXIoKHZhbHVlLCBpbmRleCwgc2VsZikgPT4gc2VsZi5pbmRleE9mKHZhbHVlKSA9PT0gaW5kZXgpO1xuICAgICAgY29uc3QgbnBtTW9kdWxlQW5kVmVyc2lvbiA9IE9iamVjdC5mcm9tRW50cmllcyhucG1Nb2R1bGVzLm1hcCgobW9kdWxlKSA9PiBbbW9kdWxlLCBnZXRWZXJzaW9uKG1vZHVsZSldKSk7XG4gICAgICBjb25zdCBjdmRscyA9IE9iamVjdC5mcm9tRW50cmllcyhcbiAgICAgICAgbnBtTW9kdWxlc1xuICAgICAgICAgIC5maWx0ZXIoKG1vZHVsZSkgPT4gZ2V0Q3ZkbE5hbWUobW9kdWxlKSAhPSBudWxsKVxuICAgICAgICAgIC5tYXAoKG1vZHVsZSkgPT4gW21vZHVsZSwgeyBuYW1lOiBnZXRDdmRsTmFtZShtb2R1bGUpLCB2ZXJzaW9uOiBnZXRWZXJzaW9uKG1vZHVsZSkgfV0pXG4gICAgICApO1xuXG4gICAgICBta2RpclN5bmMocGF0aC5kaXJuYW1lKHN0YXRzRmlsZSksIHsgcmVjdXJzaXZlOiB0cnVlIH0pO1xuICAgICAgY29uc3QgcHJvamVjdFBhY2thZ2VKc29uID0gSlNPTi5wYXJzZShyZWFkRmlsZVN5bmMocHJvamVjdFBhY2thZ2VKc29uRmlsZSwgeyBlbmNvZGluZzogJ3V0Zi04JyB9KSk7XG5cbiAgICAgIGNvbnN0IGVudHJ5U2NyaXB0cyA9IE9iamVjdC52YWx1ZXMoYnVuZGxlKVxuICAgICAgICAuZmlsdGVyKChidW5kbGUpID0+IGJ1bmRsZS5pc0VudHJ5KVxuICAgICAgICAubWFwKChidW5kbGUpID0+IGJ1bmRsZS5maWxlTmFtZSk7XG5cbiAgICAgIGNvbnN0IGdlbmVyYXRlZEluZGV4SHRtbCA9IHBhdGgucmVzb2x2ZShidWlsZE91dHB1dEZvbGRlciwgJ2luZGV4Lmh0bWwnKTtcbiAgICAgIGNvbnN0IGN1c3RvbUluZGV4RGF0YTogc3RyaW5nID0gcmVhZEZpbGVTeW5jKHByb2plY3RJbmRleEh0bWwsIHsgZW5jb2Rpbmc6ICd1dGYtOCcgfSk7XG4gICAgICBjb25zdCBnZW5lcmF0ZWRJbmRleERhdGE6IHN0cmluZyA9IHJlYWRGaWxlU3luYyhnZW5lcmF0ZWRJbmRleEh0bWwsIHtcbiAgICAgICAgZW5jb2Rpbmc6ICd1dGYtOCdcbiAgICAgIH0pO1xuXG4gICAgICBjb25zdCBjdXN0b21JbmRleFJvd3MgPSBuZXcgU2V0KGN1c3RvbUluZGV4RGF0YS5zcGxpdCgvW1xcclxcbl0vKS5maWx0ZXIoKHJvdykgPT4gcm93LnRyaW0oKSAhPT0gJycpKTtcbiAgICAgIGNvbnN0IGdlbmVyYXRlZEluZGV4Um93cyA9IGdlbmVyYXRlZEluZGV4RGF0YS5zcGxpdCgvW1xcclxcbl0vKS5maWx0ZXIoKHJvdykgPT4gcm93LnRyaW0oKSAhPT0gJycpO1xuXG4gICAgICBjb25zdCByb3dzR2VuZXJhdGVkOiBzdHJpbmdbXSA9IFtdO1xuICAgICAgZ2VuZXJhdGVkSW5kZXhSb3dzLmZvckVhY2goKHJvdykgPT4ge1xuICAgICAgICBpZiAoIWN1c3RvbUluZGV4Um93cy5oYXMocm93KSkge1xuICAgICAgICAgIHJvd3NHZW5lcmF0ZWQucHVzaChyb3cpO1xuICAgICAgICB9XG4gICAgICB9KTtcblxuICAgICAgLy9BZnRlciBkZXYtYnVuZGxlIGJ1aWxkIGFkZCB1c2VkIEZsb3cgZnJvbnRlbmQgaW1wb3J0cyBKc01vZHVsZS9KYXZhU2NyaXB0L0Nzc0ltcG9ydFxuXG4gICAgICBjb25zdCBwYXJzZUltcG9ydHMgPSAoZmlsZW5hbWU6IHN0cmluZywgcmVzdWx0OiBTZXQ8c3RyaW5nPik6IHZvaWQgPT4ge1xuICAgICAgICBjb25zdCBjb250ZW50OiBzdHJpbmcgPSByZWFkRmlsZVN5bmMoZmlsZW5hbWUsIHsgZW5jb2Rpbmc6ICd1dGYtOCcgfSk7XG4gICAgICAgIGNvbnN0IGxpbmVzID0gY29udGVudC5zcGxpdCgnXFxuJyk7XG4gICAgICAgIGNvbnN0IHN0YXRpY0ltcG9ydHMgPSBsaW5lc1xuICAgICAgICAgIC5maWx0ZXIoKGxpbmUpID0+IGxpbmUuc3RhcnRzV2l0aCgnaW1wb3J0ICcpKVxuICAgICAgICAgIC5tYXAoKGxpbmUpID0+IGxpbmUuc3Vic3RyaW5nKGxpbmUuaW5kZXhPZihcIidcIikgKyAxLCBsaW5lLmxhc3RJbmRleE9mKFwiJ1wiKSkpXG4gICAgICAgICAgLm1hcCgobGluZSkgPT4gKGxpbmUuaW5jbHVkZXMoJz8nKSA/IGxpbmUuc3Vic3RyaW5nKDAsIGxpbmUubGFzdEluZGV4T2YoJz8nKSkgOiBsaW5lKSk7XG4gICAgICAgIGNvbnN0IGR5bmFtaWNJbXBvcnRzID0gbGluZXNcbiAgICAgICAgICAuZmlsdGVyKChsaW5lKSA9PiBsaW5lLmluY2x1ZGVzKCdpbXBvcnQoJykpXG4gICAgICAgICAgLm1hcCgobGluZSkgPT4gbGluZS5yZXBsYWNlKC8uKmltcG9ydFxcKC8sICcnKSlcbiAgICAgICAgICAubWFwKChsaW5lKSA9PiBsaW5lLnNwbGl0KC8nLylbMV0pXG4gICAgICAgICAgLm1hcCgobGluZSkgPT4gKGxpbmUuaW5jbHVkZXMoJz8nKSA/IGxpbmUuc3Vic3RyaW5nKDAsIGxpbmUubGFzdEluZGV4T2YoJz8nKSkgOiBsaW5lKSk7XG5cbiAgICAgICAgc3RhdGljSW1wb3J0cy5mb3JFYWNoKChzdGF0aWNJbXBvcnQpID0+IHJlc3VsdC5hZGQoc3RhdGljSW1wb3J0KSk7XG5cbiAgICAgICAgZHluYW1pY0ltcG9ydHMubWFwKChkeW5hbWljSW1wb3J0KSA9PiB7XG4gICAgICAgICAgY29uc3QgaW1wb3J0ZWRGaWxlID0gcGF0aC5yZXNvbHZlKHBhdGguZGlybmFtZShmaWxlbmFtZSksIGR5bmFtaWNJbXBvcnQpO1xuICAgICAgICAgIHBhcnNlSW1wb3J0cyhpbXBvcnRlZEZpbGUsIHJlc3VsdCk7XG4gICAgICAgIH0pO1xuICAgICAgfTtcblxuICAgICAgY29uc3QgZ2VuZXJhdGVkSW1wb3J0c1NldCA9IG5ldyBTZXQ8c3RyaW5nPigpO1xuICAgICAgcGFyc2VJbXBvcnRzKFxuICAgICAgICBwYXRoLnJlc29sdmUodGhlbWVPcHRpb25zLmZyb250ZW5kR2VuZXJhdGVkRm9sZGVyLCAnZmxvdycsICdnZW5lcmF0ZWQtZmxvdy1pbXBvcnRzLmpzJyksXG4gICAgICAgIGdlbmVyYXRlZEltcG9ydHNTZXRcbiAgICAgICk7XG4gICAgICBjb25zdCBnZW5lcmF0ZWRJbXBvcnRzID0gQXJyYXkuZnJvbShnZW5lcmF0ZWRJbXBvcnRzU2V0KS5zb3J0KCk7XG5cbiAgICAgIGNvbnN0IGZyb250ZW5kRmlsZXM6IFJlY29yZDxzdHJpbmcsIHN0cmluZz4gPSB7fTtcblxuICAgICAgY29uc3QgcHJvamVjdEZpbGVFeHRlbnNpb25zID0gWycuanMnLCAnLmpzLm1hcCcsICcudHMnLCAnLnRzLm1hcCcsICcudHN4JywgJy50c3gubWFwJywgJy5jc3MnLCAnLmNzcy5tYXAnXTtcblxuICAgICAgY29uc3QgaXNUaGVtZUNvbXBvbmVudHNSZXNvdXJjZSA9IChpZDogc3RyaW5nKSA9PlxuICAgICAgICAgIGlkLnN0YXJ0c1dpdGgodGhlbWVPcHRpb25zLmZyb250ZW5kR2VuZXJhdGVkRm9sZGVyLnJlcGxhY2UoL1xcXFwvZywgJy8nKSlcbiAgICAgICAgICAgICAgJiYgaWQubWF0Y2goLy4qXFwvamFyLXJlc291cmNlc1xcL3RoZW1lc1xcL1teXFwvXStcXC9jb21wb25lbnRzXFwvLyk7XG5cbiAgICAgIC8vIGNvbGxlY3RzIHByb2plY3QncyBmcm9udGVuZCByZXNvdXJjZXMgaW4gZnJvbnRlbmQgZm9sZGVyLCBleGNsdWRpbmdcbiAgICAgIC8vICdnZW5lcmF0ZWQnIHN1Yi1mb2xkZXIsIGV4Y2VwdCBmb3IgbGVnYWN5IHNoYWRvdyBET00gc3R5bGVzaGVldHNcbiAgICAgIC8vIHBhY2thZ2VkIGluIGB0aGVtZS9jb21wb25lbnRzL2AgZm9sZGVyLlxuICAgICAgbW9kdWxlc1xuICAgICAgICAubWFwKChpZCkgPT4gaWQucmVwbGFjZSgvXFxcXC9nLCAnLycpKVxuICAgICAgICAuZmlsdGVyKChpZCkgPT4gaWQuc3RhcnRzV2l0aChmcm9udGVuZEZvbGRlci5yZXBsYWNlKC9cXFxcL2csICcvJykpKVxuICAgICAgICAuZmlsdGVyKChpZCkgPT4gIWlkLnN0YXJ0c1dpdGgodGhlbWVPcHRpb25zLmZyb250ZW5kR2VuZXJhdGVkRm9sZGVyLnJlcGxhY2UoL1xcXFwvZywgJy8nKSkgfHwgaXNUaGVtZUNvbXBvbmVudHNSZXNvdXJjZShpZCkpXG4gICAgICAgIC5tYXAoKGlkKSA9PiBpZC5zdWJzdHJpbmcoZnJvbnRlbmRGb2xkZXIubGVuZ3RoICsgMSkpXG4gICAgICAgIC5tYXAoKGxpbmU6IHN0cmluZykgPT4gKGxpbmUuaW5jbHVkZXMoJz8nKSA/IGxpbmUuc3Vic3RyaW5nKDAsIGxpbmUubGFzdEluZGV4T2YoJz8nKSkgOiBsaW5lKSlcbiAgICAgICAgLmZvckVhY2goKGxpbmU6IHN0cmluZykgPT4ge1xuICAgICAgICAgIC8vIFxcclxcbiBmcm9tIHdpbmRvd3MgbWFkZSBmaWxlcyBtYXkgYmUgdXNlZCBzbyBjaGFuZ2UgdG8gXFxuXG4gICAgICAgICAgY29uc3QgZmlsZVBhdGggPSBwYXRoLnJlc29sdmUoZnJvbnRlbmRGb2xkZXIsIGxpbmUpO1xuICAgICAgICAgIGlmIChwcm9qZWN0RmlsZUV4dGVuc2lvbnMuaW5jbHVkZXMocGF0aC5leHRuYW1lKGZpbGVQYXRoKSkpIHtcbiAgICAgICAgICAgIGNvbnN0IGZpbGVCdWZmZXIgPSByZWFkRmlsZVN5bmMoZmlsZVBhdGgsIHsgZW5jb2Rpbmc6ICd1dGYtOCcgfSkucmVwbGFjZSgvXFxyXFxuL2csICdcXG4nKTtcbiAgICAgICAgICAgIGZyb250ZW5kRmlsZXNbbGluZV0gPSBjcmVhdGVIYXNoKCdzaGEyNTYnKS51cGRhdGUoZmlsZUJ1ZmZlciwgJ3V0ZjgnKS5kaWdlc3QoJ2hleCcpO1xuICAgICAgICAgIH1cbiAgICAgICAgfSk7XG5cbiAgICAgIC8vIGNvbGxlY3RzIGZyb250ZW5kIHJlc291cmNlcyBmcm9tIHRoZSBKQVJzXG4gICAgICBnZW5lcmF0ZWRJbXBvcnRzXG4gICAgICAgIC5maWx0ZXIoKGxpbmU6IHN0cmluZykgPT4gbGluZS5pbmNsdWRlcygnZ2VuZXJhdGVkL2phci1yZXNvdXJjZXMnKSlcbiAgICAgICAgLmZvckVhY2goKGxpbmU6IHN0cmluZykgPT4ge1xuICAgICAgICAgIGxldCBmaWxlbmFtZSA9IGxpbmUuc3Vic3RyaW5nKGxpbmUuaW5kZXhPZignZ2VuZXJhdGVkJykpO1xuICAgICAgICAgIC8vIFxcclxcbiBmcm9tIHdpbmRvd3MgbWFkZSBmaWxlcyBtYXkgYmUgdXNlZCBybyByZW1vdmUgdG8gYmUgb25seSBcXG5cbiAgICAgICAgICBjb25zdCBmaWxlQnVmZmVyID0gcmVhZEZpbGVTeW5jKHBhdGgucmVzb2x2ZShmcm9udGVuZEZvbGRlciwgZmlsZW5hbWUpLCB7IGVuY29kaW5nOiAndXRmLTgnIH0pLnJlcGxhY2UoXG4gICAgICAgICAgICAvXFxyXFxuL2csXG4gICAgICAgICAgICAnXFxuJ1xuICAgICAgICAgICk7XG4gICAgICAgICAgY29uc3QgaGFzaCA9IGNyZWF0ZUhhc2goJ3NoYTI1NicpLnVwZGF0ZShmaWxlQnVmZmVyLCAndXRmOCcpLmRpZ2VzdCgnaGV4Jyk7XG5cbiAgICAgICAgICBjb25zdCBmaWxlS2V5ID0gbGluZS5zdWJzdHJpbmcobGluZS5pbmRleE9mKCdqYXItcmVzb3VyY2VzLycpICsgMTQpO1xuICAgICAgICAgIGZyb250ZW5kRmlsZXNbZmlsZUtleV0gPSBoYXNoO1xuICAgICAgICB9KTtcbiAgICAgIC8vIElmIGEgaW5kZXgudHMgZXhpc3RzIGhhc2ggaXQgdG8gYmUgYWJsZSB0byBzZWUgaWYgaXQgY2hhbmdlcy5cbiAgICAgIGlmIChleGlzdHNTeW5jKHBhdGgucmVzb2x2ZShmcm9udGVuZEZvbGRlciwgJ2luZGV4LnRzJykpKSB7XG4gICAgICAgIGNvbnN0IGZpbGVCdWZmZXIgPSByZWFkRmlsZVN5bmMocGF0aC5yZXNvbHZlKGZyb250ZW5kRm9sZGVyLCAnaW5kZXgudHMnKSwgeyBlbmNvZGluZzogJ3V0Zi04JyB9KS5yZXBsYWNlKFxuICAgICAgICAgIC9cXHJcXG4vZyxcbiAgICAgICAgICAnXFxuJ1xuICAgICAgICApO1xuICAgICAgICBmcm9udGVuZEZpbGVzW2BpbmRleC50c2BdID0gY3JlYXRlSGFzaCgnc2hhMjU2JykudXBkYXRlKGZpbGVCdWZmZXIsICd1dGY4JykuZGlnZXN0KCdoZXgnKTtcbiAgICAgIH1cblxuICAgICAgY29uc3QgdGhlbWVKc29uQ29udGVudHM6IFJlY29yZDxzdHJpbmcsIHN0cmluZz4gPSB7fTtcbiAgICAgIGNvbnN0IHRoZW1lc0ZvbGRlciA9IHBhdGgucmVzb2x2ZShqYXJSZXNvdXJjZXNGb2xkZXIsICd0aGVtZXMnKTtcbiAgICAgIGlmIChleGlzdHNTeW5jKHRoZW1lc0ZvbGRlcikpIHtcbiAgICAgICAgcmVhZGRpclN5bmModGhlbWVzRm9sZGVyKS5mb3JFYWNoKCh0aGVtZUZvbGRlcikgPT4ge1xuICAgICAgICAgIGNvbnN0IHRoZW1lSnNvbiA9IHBhdGgucmVzb2x2ZSh0aGVtZXNGb2xkZXIsIHRoZW1lRm9sZGVyLCAndGhlbWUuanNvbicpO1xuICAgICAgICAgIGlmIChleGlzdHNTeW5jKHRoZW1lSnNvbikpIHtcbiAgICAgICAgICAgIHRoZW1lSnNvbkNvbnRlbnRzW3BhdGguYmFzZW5hbWUodGhlbWVGb2xkZXIpXSA9IHJlYWRGaWxlU3luYyh0aGVtZUpzb24sIHsgZW5jb2Rpbmc6ICd1dGYtOCcgfSkucmVwbGFjZShcbiAgICAgICAgICAgICAgL1xcclxcbi9nLFxuICAgICAgICAgICAgICAnXFxuJ1xuICAgICAgICAgICAgKTtcbiAgICAgICAgICB9XG4gICAgICAgIH0pO1xuICAgICAgfVxuXG4gICAgICBjb2xsZWN0VGhlbWVKc29uc0luRnJvbnRlbmQodGhlbWVKc29uQ29udGVudHMsIHNldHRpbmdzLnRoZW1lTmFtZSk7XG5cbiAgICAgIGxldCB3ZWJDb21wb25lbnRzOiBzdHJpbmdbXSA9IFtdO1xuICAgICAgaWYgKHdlYkNvbXBvbmVudFRhZ3MpIHtcbiAgICAgICAgd2ViQ29tcG9uZW50cyA9IHdlYkNvbXBvbmVudFRhZ3Muc3BsaXQoJzsnKTtcbiAgICAgIH1cblxuICAgICAgY29uc3Qgc3RhdHMgPSB7XG4gICAgICAgIHBhY2thZ2VKc29uRGVwZW5kZW5jaWVzOiBwcm9qZWN0UGFja2FnZUpzb24uZGVwZW5kZW5jaWVzLFxuICAgICAgICBucG1Nb2R1bGVzOiBucG1Nb2R1bGVBbmRWZXJzaW9uLFxuICAgICAgICBidW5kbGVJbXBvcnRzOiBnZW5lcmF0ZWRJbXBvcnRzLFxuICAgICAgICBmcm9udGVuZEhhc2hlczogZnJvbnRlbmRGaWxlcyxcbiAgICAgICAgdGhlbWVKc29uQ29udGVudHM6IHRoZW1lSnNvbkNvbnRlbnRzLFxuICAgICAgICBlbnRyeVNjcmlwdHMsXG4gICAgICAgIHdlYkNvbXBvbmVudHMsXG4gICAgICAgIGN2ZGxNb2R1bGVzOiBjdmRscyxcbiAgICAgICAgcGFja2FnZUpzb25IYXNoOiBwcm9qZWN0UGFja2FnZUpzb24/LnZhYWRpbj8uaGFzaCxcbiAgICAgICAgaW5kZXhIdG1sR2VuZXJhdGVkOiByb3dzR2VuZXJhdGVkXG4gICAgICB9O1xuICAgICAgd3JpdGVGaWxlU3luYyhzdGF0c0ZpbGUsIEpTT04uc3RyaW5naWZ5KHN0YXRzLCBudWxsLCAxKSk7XG4gICAgfVxuICB9O1xufVxuZnVuY3Rpb24gdmFhZGluQnVuZGxlc1BsdWdpbigpOiBQbHVnaW5PcHRpb24ge1xuICB0eXBlIEV4cG9ydEluZm8gPVxuICAgIHwgc3RyaW5nXG4gICAgfCB7XG4gICAgICAgIG5hbWVzcGFjZT86IHN0cmluZztcbiAgICAgICAgc291cmNlOiBzdHJpbmc7XG4gICAgICB9O1xuXG4gIHR5cGUgRXhwb3NlSW5mbyA9IHtcbiAgICBleHBvcnRzOiBFeHBvcnRJbmZvW107XG4gIH07XG5cbiAgdHlwZSBQYWNrYWdlSW5mbyA9IHtcbiAgICB2ZXJzaW9uOiBzdHJpbmc7XG4gICAgZXhwb3NlczogUmVjb3JkPHN0cmluZywgRXhwb3NlSW5mbz47XG4gIH07XG5cbiAgdHlwZSBCdW5kbGVKc29uID0ge1xuICAgIHBhY2thZ2VzOiBSZWNvcmQ8c3RyaW5nLCBQYWNrYWdlSW5mbz47XG4gIH07XG5cbiAgY29uc3QgZGlzYWJsZWRNZXNzYWdlID0gJ1ZhYWRpbiBjb21wb25lbnQgZGVwZW5kZW5jeSBidW5kbGVzIGFyZSBkaXNhYmxlZC4nO1xuXG4gIGNvbnN0IG1vZHVsZXNEaXJlY3RvcnkgPSBub2RlTW9kdWxlc0ZvbGRlci5yZXBsYWNlKC9cXFxcL2csICcvJyk7XG5cbiAgbGV0IHZhYWRpbkJ1bmRsZUpzb246IEJ1bmRsZUpzb247XG5cbiAgZnVuY3Rpb24gcGFyc2VNb2R1bGVJZChpZDogc3RyaW5nKTogeyBwYWNrYWdlTmFtZTogc3RyaW5nOyBtb2R1bGVQYXRoOiBzdHJpbmcgfSB7XG4gICAgY29uc3QgW3Njb3BlLCBzY29wZWRQYWNrYWdlTmFtZV0gPSBpZC5zcGxpdCgnLycsIDMpO1xuICAgIGNvbnN0IHBhY2thZ2VOYW1lID0gc2NvcGUuc3RhcnRzV2l0aCgnQCcpID8gYCR7c2NvcGV9LyR7c2NvcGVkUGFja2FnZU5hbWV9YCA6IHNjb3BlO1xuICAgIGNvbnN0IG1vZHVsZVBhdGggPSBgLiR7aWQuc3Vic3RyaW5nKHBhY2thZ2VOYW1lLmxlbmd0aCl9YDtcbiAgICByZXR1cm4ge1xuICAgICAgcGFja2FnZU5hbWUsXG4gICAgICBtb2R1bGVQYXRoXG4gICAgfTtcbiAgfVxuXG4gIGZ1bmN0aW9uIGdldEV4cG9ydHMoaWQ6IHN0cmluZyk6IHN0cmluZ1tdIHwgdW5kZWZpbmVkIHtcbiAgICBjb25zdCB7IHBhY2thZ2VOYW1lLCBtb2R1bGVQYXRoIH0gPSBwYXJzZU1vZHVsZUlkKGlkKTtcbiAgICBjb25zdCBwYWNrYWdlSW5mbyA9IHZhYWRpbkJ1bmRsZUpzb24ucGFja2FnZXNbcGFja2FnZU5hbWVdO1xuXG4gICAgaWYgKCFwYWNrYWdlSW5mbykgcmV0dXJuO1xuXG4gICAgY29uc3QgZXhwb3NlSW5mbzogRXhwb3NlSW5mbyA9IHBhY2thZ2VJbmZvLmV4cG9zZXNbbW9kdWxlUGF0aF07XG4gICAgaWYgKCFleHBvc2VJbmZvKSByZXR1cm47XG5cbiAgICBjb25zdCBleHBvcnRzU2V0ID0gbmV3IFNldDxzdHJpbmc+KCk7XG4gICAgZm9yIChjb25zdCBlIG9mIGV4cG9zZUluZm8uZXhwb3J0cykge1xuICAgICAgaWYgKHR5cGVvZiBlID09PSAnc3RyaW5nJykge1xuICAgICAgICBleHBvcnRzU2V0LmFkZChlKTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIGNvbnN0IHsgbmFtZXNwYWNlLCBzb3VyY2UgfSA9IGU7XG4gICAgICAgIGlmIChuYW1lc3BhY2UpIHtcbiAgICAgICAgICBleHBvcnRzU2V0LmFkZChuYW1lc3BhY2UpO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIGNvbnN0IHNvdXJjZUV4cG9ydHMgPSBnZXRFeHBvcnRzKHNvdXJjZSk7XG4gICAgICAgICAgaWYgKHNvdXJjZUV4cG9ydHMpIHtcbiAgICAgICAgICAgIHNvdXJjZUV4cG9ydHMuZm9yRWFjaCgoZSkgPT4gZXhwb3J0c1NldC5hZGQoZSkpO1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgfVxuICAgIH1cbiAgICByZXR1cm4gQXJyYXkuZnJvbShleHBvcnRzU2V0KTtcbiAgfVxuXG4gIGZ1bmN0aW9uIGdldEV4cG9ydEJpbmRpbmcoYmluZGluZzogc3RyaW5nKSB7XG4gICAgcmV0dXJuIGJpbmRpbmcgPT09ICdkZWZhdWx0JyA/ICdfZGVmYXVsdCBhcyBkZWZhdWx0JyA6IGJpbmRpbmc7XG4gIH1cblxuICBmdW5jdGlvbiBnZXRJbXBvcnRBc3NpZ21lbnQoYmluZGluZzogc3RyaW5nKSB7XG4gICAgcmV0dXJuIGJpbmRpbmcgPT09ICdkZWZhdWx0JyA/ICdkZWZhdWx0OiBfZGVmYXVsdCcgOiBiaW5kaW5nO1xuICB9XG5cbiAgcmV0dXJuIHtcbiAgICBuYW1lOiAndmFhZGluOmJ1bmRsZXMnLFxuICAgIGVuZm9yY2U6ICdwcmUnLFxuICAgIGFwcGx5KGNvbmZpZywgeyBjb21tYW5kIH0pIHtcbiAgICAgIGlmIChjb21tYW5kICE9PSAnc2VydmUnKSByZXR1cm4gZmFsc2U7XG5cbiAgICAgIHRyeSB7XG4gICAgICAgIGNvbnN0IHZhYWRpbkJ1bmRsZUpzb25QYXRoID0gcmVxdWlyZS5yZXNvbHZlKCdAdmFhZGluL2J1bmRsZXMvdmFhZGluLWJ1bmRsZS5qc29uJyk7XG4gICAgICAgIHZhYWRpbkJ1bmRsZUpzb24gPSBKU09OLnBhcnNlKHJlYWRGaWxlU3luYyh2YWFkaW5CdW5kbGVKc29uUGF0aCwgeyBlbmNvZGluZzogJ3V0ZjgnIH0pKTtcbiAgICAgIH0gY2F0Y2ggKGU6IHVua25vd24pIHtcbiAgICAgICAgaWYgKHR5cGVvZiBlID09PSAnb2JqZWN0JyAmJiAoZSBhcyB7IGNvZGU6IHN0cmluZyB9KS5jb2RlID09PSAnTU9EVUxFX05PVF9GT1VORCcpIHtcbiAgICAgICAgICB2YWFkaW5CdW5kbGVKc29uID0geyBwYWNrYWdlczoge30gfTtcbiAgICAgICAgICBjb25zb2xlLmluZm8oYEB2YWFkaW4vYnVuZGxlcyBucG0gcGFja2FnZSBpcyBub3QgZm91bmQsICR7ZGlzYWJsZWRNZXNzYWdlfWApO1xuICAgICAgICAgIHJldHVybiBmYWxzZTtcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICB0aHJvdyBlO1xuICAgICAgICB9XG4gICAgICB9XG5cbiAgICAgIGNvbnN0IHZlcnNpb25NaXNtYXRjaGVzOiBBcnJheTx7IG5hbWU6IHN0cmluZzsgYnVuZGxlZFZlcnNpb246IHN0cmluZzsgaW5zdGFsbGVkVmVyc2lvbjogc3RyaW5nIH0+ID0gW107XG4gICAgICBmb3IgKGNvbnN0IFtuYW1lLCBwYWNrYWdlSW5mb10gb2YgT2JqZWN0LmVudHJpZXModmFhZGluQnVuZGxlSnNvbi5wYWNrYWdlcykpIHtcbiAgICAgICAgbGV0IGluc3RhbGxlZFZlcnNpb246IHN0cmluZyB8IHVuZGVmaW5lZCA9IHVuZGVmaW5lZDtcbiAgICAgICAgdHJ5IHtcbiAgICAgICAgICBjb25zdCB7IHZlcnNpb246IGJ1bmRsZWRWZXJzaW9uIH0gPSBwYWNrYWdlSW5mbztcbiAgICAgICAgICBjb25zdCBpbnN0YWxsZWRQYWNrYWdlSnNvbkZpbGUgPSBwYXRoLnJlc29sdmUobW9kdWxlc0RpcmVjdG9yeSwgbmFtZSwgJ3BhY2thZ2UuanNvbicpO1xuICAgICAgICAgIGNvbnN0IHBhY2thZ2VKc29uID0gSlNPTi5wYXJzZShyZWFkRmlsZVN5bmMoaW5zdGFsbGVkUGFja2FnZUpzb25GaWxlLCB7IGVuY29kaW5nOiAndXRmOCcgfSkpO1xuICAgICAgICAgIGluc3RhbGxlZFZlcnNpb24gPSBwYWNrYWdlSnNvbi52ZXJzaW9uO1xuICAgICAgICAgIGlmIChpbnN0YWxsZWRWZXJzaW9uICYmIGluc3RhbGxlZFZlcnNpb24gIT09IGJ1bmRsZWRWZXJzaW9uKSB7XG4gICAgICAgICAgICB2ZXJzaW9uTWlzbWF0Y2hlcy5wdXNoKHtcbiAgICAgICAgICAgICAgbmFtZSxcbiAgICAgICAgICAgICAgYnVuZGxlZFZlcnNpb24sXG4gICAgICAgICAgICAgIGluc3RhbGxlZFZlcnNpb25cbiAgICAgICAgICAgIH0pO1xuICAgICAgICAgIH1cbiAgICAgICAgfSBjYXRjaCAoXykge1xuICAgICAgICAgIC8vIGlnbm9yZSBwYWNrYWdlIG5vdCBmb3VuZFxuICAgICAgICB9XG4gICAgICB9XG4gICAgICBpZiAodmVyc2lvbk1pc21hdGNoZXMubGVuZ3RoKSB7XG4gICAgICAgIGNvbnNvbGUuaW5mbyhgQHZhYWRpbi9idW5kbGVzIGhhcyB2ZXJzaW9uIG1pc21hdGNoZXMgd2l0aCBpbnN0YWxsZWQgcGFja2FnZXMsICR7ZGlzYWJsZWRNZXNzYWdlfWApO1xuICAgICAgICBjb25zb2xlLmluZm8oYFBhY2thZ2VzIHdpdGggdmVyc2lvbiBtaXNtYXRjaGVzOiAke0pTT04uc3RyaW5naWZ5KHZlcnNpb25NaXNtYXRjaGVzLCB1bmRlZmluZWQsIDIpfWApO1xuICAgICAgICB2YWFkaW5CdW5kbGVKc29uID0geyBwYWNrYWdlczoge30gfTtcbiAgICAgICAgcmV0dXJuIGZhbHNlO1xuICAgICAgfVxuXG4gICAgICByZXR1cm4gdHJ1ZTtcbiAgICB9LFxuICAgIGFzeW5jIGNvbmZpZyhjb25maWcpIHtcbiAgICAgIHJldHVybiBtZXJnZUNvbmZpZyhcbiAgICAgICAge1xuICAgICAgICAgIG9wdGltaXplRGVwczoge1xuICAgICAgICAgICAgZXhjbHVkZTogW1xuICAgICAgICAgICAgICAvLyBWYWFkaW4gYnVuZGxlXG4gICAgICAgICAgICAgICdAdmFhZGluL2J1bmRsZXMnLFxuICAgICAgICAgICAgICAuLi5PYmplY3Qua2V5cyh2YWFkaW5CdW5kbGVKc29uLnBhY2thZ2VzKSxcbiAgICAgICAgICAgICAgJ0B2YWFkaW4vdmFhZGluLW1hdGVyaWFsLXN0eWxlcydcbiAgICAgICAgICAgIF1cbiAgICAgICAgICB9XG4gICAgICAgIH0sXG4gICAgICAgIGNvbmZpZ1xuICAgICAgKTtcbiAgICB9LFxuICAgIGxvYWQocmF3SWQpIHtcbiAgICAgIGNvbnN0IFtwYXRoLCBwYXJhbXNdID0gcmF3SWQuc3BsaXQoJz8nKTtcbiAgICAgIGlmICghcGF0aC5zdGFydHNXaXRoKG1vZHVsZXNEaXJlY3RvcnkpKSByZXR1cm47XG5cbiAgICAgIGNvbnN0IGlkID0gcGF0aC5zdWJzdHJpbmcobW9kdWxlc0RpcmVjdG9yeS5sZW5ndGggKyAxKTtcbiAgICAgIGNvbnN0IGJpbmRpbmdzID0gZ2V0RXhwb3J0cyhpZCk7XG4gICAgICBpZiAoYmluZGluZ3MgPT09IHVuZGVmaW5lZCkgcmV0dXJuO1xuXG4gICAgICBjb25zdCBjYWNoZVN1ZmZpeCA9IHBhcmFtcyA/IGA/JHtwYXJhbXN9YCA6ICcnO1xuICAgICAgY29uc3QgYnVuZGxlUGF0aCA9IGBAdmFhZGluL2J1bmRsZXMvdmFhZGluLmpzJHtjYWNoZVN1ZmZpeH1gO1xuXG4gICAgICByZXR1cm4gYGltcG9ydCB7IGluaXQgYXMgVmFhZGluQnVuZGxlSW5pdCwgZ2V0IGFzIFZhYWRpbkJ1bmRsZUdldCB9IGZyb20gJyR7YnVuZGxlUGF0aH0nO1xuYXdhaXQgVmFhZGluQnVuZGxlSW5pdCgnZGVmYXVsdCcpO1xuY29uc3QgeyAke2JpbmRpbmdzLm1hcChnZXRJbXBvcnRBc3NpZ21lbnQpLmpvaW4oJywgJyl9IH0gPSAoYXdhaXQgVmFhZGluQnVuZGxlR2V0KCcuL25vZGVfbW9kdWxlcy8ke2lkfScpKSgpO1xuZXhwb3J0IHsgJHtiaW5kaW5ncy5tYXAoZ2V0RXhwb3J0QmluZGluZykuam9pbignLCAnKX0gfTtgO1xuICAgIH1cbiAgfTtcbn1cblxuZnVuY3Rpb24gdGhlbWVQbHVnaW4ob3B0cyk6IFBsdWdpbk9wdGlvbiB7XG4gIGNvbnN0IGZ1bGxUaGVtZU9wdGlvbnMgPSB7IC4uLnRoZW1lT3B0aW9ucywgZGV2TW9kZTogb3B0cy5kZXZNb2RlIH07XG4gIHJldHVybiB7XG4gICAgbmFtZTogJ3ZhYWRpbjp0aGVtZScsXG4gICAgY29uZmlnKCkge1xuICAgICAgcHJvY2Vzc1RoZW1lUmVzb3VyY2VzKGZ1bGxUaGVtZU9wdGlvbnMsIGNvbnNvbGUpO1xuICAgIH0sXG4gICAgY29uZmlndXJlU2VydmVyKHNlcnZlcikge1xuICAgICAgZnVuY3Rpb24gaGFuZGxlVGhlbWVGaWxlQ3JlYXRlRGVsZXRlKHRoZW1lRmlsZSwgc3RhdHMpIHtcbiAgICAgICAgaWYgKHRoZW1lRmlsZS5zdGFydHNXaXRoKHRoZW1lRm9sZGVyKSkge1xuICAgICAgICAgIGNvbnN0IGNoYW5nZWQgPSBwYXRoLnJlbGF0aXZlKHRoZW1lRm9sZGVyLCB0aGVtZUZpbGUpO1xuICAgICAgICAgIGNvbnNvbGUuZGVidWcoJ1RoZW1lIGZpbGUgJyArICghIXN0YXRzID8gJ2NyZWF0ZWQnIDogJ2RlbGV0ZWQnKSwgY2hhbmdlZCk7XG4gICAgICAgICAgcHJvY2Vzc1RoZW1lUmVzb3VyY2VzKGZ1bGxUaGVtZU9wdGlvbnMsIGNvbnNvbGUpO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgICBzZXJ2ZXIud2F0Y2hlci5vbignYWRkJywgaGFuZGxlVGhlbWVGaWxlQ3JlYXRlRGVsZXRlKTtcbiAgICAgIHNlcnZlci53YXRjaGVyLm9uKCd1bmxpbmsnLCBoYW5kbGVUaGVtZUZpbGVDcmVhdGVEZWxldGUpO1xuICAgIH0sXG4gICAgaGFuZGxlSG90VXBkYXRlKGNvbnRleHQpIHtcbiAgICAgIGNvbnN0IGNvbnRleHRQYXRoID0gcGF0aC5yZXNvbHZlKGNvbnRleHQuZmlsZSk7XG4gICAgICBjb25zdCB0aGVtZVBhdGggPSBwYXRoLnJlc29sdmUodGhlbWVGb2xkZXIpO1xuICAgICAgaWYgKGNvbnRleHRQYXRoLnN0YXJ0c1dpdGgodGhlbWVQYXRoKSkge1xuICAgICAgICBjb25zdCBjaGFuZ2VkID0gcGF0aC5yZWxhdGl2ZSh0aGVtZVBhdGgsIGNvbnRleHRQYXRoKTtcblxuICAgICAgICBjb25zb2xlLmRlYnVnKCdUaGVtZSBmaWxlIGNoYW5nZWQnLCBjaGFuZ2VkKTtcblxuICAgICAgICBpZiAoY2hhbmdlZC5zdGFydHNXaXRoKHNldHRpbmdzLnRoZW1lTmFtZSkpIHtcbiAgICAgICAgICBwcm9jZXNzVGhlbWVSZXNvdXJjZXMoZnVsbFRoZW1lT3B0aW9ucywgY29uc29sZSk7XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICB9LFxuICAgIGFzeW5jIHJlc29sdmVJZChpZCwgaW1wb3J0ZXIpIHtcbiAgICAgIC8vIGZvcmNlIHRoZW1lIGdlbmVyYXRpb24gaWYgZ2VuZXJhdGVkIHRoZW1lIHNvdXJjZXMgZG9lcyBub3QgeWV0IGV4aXN0XG4gICAgICAvLyB0aGlzIG1heSBoYXBwZW4gZm9yIGV4YW1wbGUgZHVyaW5nIEphdmEgaG90IHJlbG9hZCB3aGVuIHVwZGF0aW5nXG4gICAgICAvLyBAVGhlbWUgYW5ub3RhdGlvbiB2YWx1ZVxuICAgICAgaWYgKFxuICAgICAgICBwYXRoLnJlc29sdmUodGhlbWVPcHRpb25zLmZyb250ZW5kR2VuZXJhdGVkRm9sZGVyLCAndGhlbWUuanMnKSA9PT0gaW1wb3J0ZXIgJiZcbiAgICAgICAgIWV4aXN0c1N5bmMocGF0aC5yZXNvbHZlKHRoZW1lT3B0aW9ucy5mcm9udGVuZEdlbmVyYXRlZEZvbGRlciwgaWQpKVxuICAgICAgKSB7XG4gICAgICAgIGNvbnNvbGUuZGVidWcoJ0dlbmVyYXRlIHRoZW1lIGZpbGUgJyArIGlkICsgJyBub3QgZXhpc3RpbmcuIFByb2Nlc3NpbmcgdGhlbWUgcmVzb3VyY2UnKTtcbiAgICAgICAgcHJvY2Vzc1RoZW1lUmVzb3VyY2VzKGZ1bGxUaGVtZU9wdGlvbnMsIGNvbnNvbGUpO1xuICAgICAgICByZXR1cm47XG4gICAgICB9XG4gICAgICBpZiAoIWlkLnN0YXJ0c1dpdGgoc2V0dGluZ3MudGhlbWVGb2xkZXIpKSB7XG4gICAgICAgIHJldHVybjtcbiAgICAgIH1cblxuICAgICAgZm9yIChjb25zdCBsb2NhdGlvbiBvZiBbdGhlbWVSZXNvdXJjZUZvbGRlciwgZnJvbnRlbmRGb2xkZXJdKSB7XG4gICAgICAgIGNvbnN0IHJlc3VsdCA9IGF3YWl0IHRoaXMucmVzb2x2ZShwYXRoLnJlc29sdmUobG9jYXRpb24sIGlkKSk7XG4gICAgICAgIGlmIChyZXN1bHQpIHtcbiAgICAgICAgICByZXR1cm4gcmVzdWx0O1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfSxcbiAgICBhc3luYyB0cmFuc2Zvcm0ocmF3LCBpZCwgb3B0aW9ucykge1xuICAgICAgLy8gcmV3cml0ZSB1cmxzIGZvciB0aGUgYXBwbGljYXRpb24gdGhlbWUgY3NzIGZpbGVzXG4gICAgICBjb25zdCBbYmFyZUlkLCBxdWVyeV0gPSBpZC5zcGxpdCgnPycpO1xuICAgICAgaWYgKFxuICAgICAgICAoIWJhcmVJZD8uc3RhcnRzV2l0aCh0aGVtZUZvbGRlcikgJiYgIWJhcmVJZD8uc3RhcnRzV2l0aCh0aGVtZU9wdGlvbnMudGhlbWVSZXNvdXJjZUZvbGRlcikpIHx8XG4gICAgICAgICFiYXJlSWQ/LmVuZHNXaXRoKCcuY3NzJylcbiAgICAgICkge1xuICAgICAgICByZXR1cm47XG4gICAgICB9XG4gICAgICBjb25zdCBbdGhlbWVOYW1lXSA9IGJhcmVJZC5zdWJzdHJpbmcodGhlbWVGb2xkZXIubGVuZ3RoICsgMSkuc3BsaXQoJy8nKTtcbiAgICAgIHJldHVybiByZXdyaXRlQ3NzVXJscyhyYXcsIHBhdGguZGlybmFtZShiYXJlSWQpLCBwYXRoLnJlc29sdmUodGhlbWVGb2xkZXIsIHRoZW1lTmFtZSksIGNvbnNvbGUsIG9wdHMpO1xuICAgIH1cbiAgfTtcbn1cblxuZnVuY3Rpb24gcnVuV2F0Y2hEb2cod2F0Y2hEb2dQb3J0LCB3YXRjaERvZ0hvc3QpIHtcbiAgY29uc3QgY2xpZW50ID0gbmV0LlNvY2tldCgpO1xuICBjbGllbnQuc2V0RW5jb2RpbmcoJ3V0ZjgnKTtcbiAgY2xpZW50Lm9uKCdlcnJvcicsIGZ1bmN0aW9uIChlcnIpIHtcbiAgICBjb25zb2xlLmxvZygnV2F0Y2hkb2cgY29ubmVjdGlvbiBlcnJvci4gVGVybWluYXRpbmcgdml0ZSBwcm9jZXNzLi4uJywgZXJyKTtcbiAgICBjbGllbnQuZGVzdHJveSgpO1xuICAgIHByb2Nlc3MuZXhpdCgwKTtcbiAgfSk7XG4gIGNsaWVudC5vbignY2xvc2UnLCBmdW5jdGlvbiAoKSB7XG4gICAgY2xpZW50LmRlc3Ryb3koKTtcbiAgICBydW5XYXRjaERvZyh3YXRjaERvZ1BvcnQsIHdhdGNoRG9nSG9zdCk7XG4gIH0pO1xuXG4gIGNsaWVudC5jb25uZWN0KHdhdGNoRG9nUG9ydCwgd2F0Y2hEb2dIb3N0IHx8ICdsb2NhbGhvc3QnKTtcbn1cblxubGV0IHNwYU1pZGRsZXdhcmVGb3JjZVJlbW92ZWQgPSBmYWxzZTtcblxuY29uc3QgYWxsb3dlZEZyb250ZW5kRm9sZGVycyA9IFtmcm9udGVuZEZvbGRlciwgbm9kZU1vZHVsZXNGb2xkZXJdO1xuXG5mdW5jdGlvbiBzaG93UmVjb21waWxlUmVhc29uKCk6IFBsdWdpbk9wdGlvbiB7XG4gIHJldHVybiB7XG4gICAgbmFtZTogJ3ZhYWRpbjp3aHkteW91LWNvbXBpbGUnLFxuICAgIGhhbmRsZUhvdFVwZGF0ZShjb250ZXh0KSB7XG4gICAgICBjb25zb2xlLmxvZygnUmVjb21waWxpbmcgYmVjYXVzZScsIGNvbnRleHQuZmlsZSwgJ2NoYW5nZWQnKTtcbiAgICB9XG4gIH07XG59XG5cbmNvbnN0IERFVl9NT0RFX1NUQVJUX1JFR0VYUCA9IC9cXC9cXCpbXFwqIV1cXHMrdmFhZGluLWRldi1tb2RlOnN0YXJ0LztcbmNvbnN0IERFVl9NT0RFX0NPREVfUkVHRVhQID0gL1xcL1xcKltcXCohXVxccyt2YWFkaW4tZGV2LW1vZGU6c3RhcnQoW1xcc1xcU10qKXZhYWRpbi1kZXYtbW9kZTplbmRcXHMrXFwqXFwqXFwvL2k7XG5cbmZ1bmN0aW9uIHByZXNlcnZlVXNhZ2VTdGF0cygpIHtcbiAgcmV0dXJuIHtcbiAgICBuYW1lOiAndmFhZGluOnByZXNlcnZlLXVzYWdlLXN0YXRzJyxcblxuICAgIHRyYW5zZm9ybShzcmM6IHN0cmluZywgaWQ6IHN0cmluZykge1xuICAgICAgaWYgKGlkLmluY2x1ZGVzKCd2YWFkaW4tdXNhZ2Utc3RhdGlzdGljcycpKSB7XG4gICAgICAgIGlmIChzcmMuaW5jbHVkZXMoJ3ZhYWRpbi1kZXYtbW9kZTpzdGFydCcpKSB7XG4gICAgICAgICAgY29uc3QgbmV3U3JjID0gc3JjLnJlcGxhY2UoREVWX01PREVfU1RBUlRfUkVHRVhQLCAnLyohIHZhYWRpbi1kZXYtbW9kZTpzdGFydCcpO1xuICAgICAgICAgIGlmIChuZXdTcmMgPT09IHNyYykge1xuICAgICAgICAgICAgY29uc29sZS5lcnJvcignQ29tbWVudCByZXBsYWNlbWVudCBmYWlsZWQgdG8gY2hhbmdlIGFueXRoaW5nJyk7XG4gICAgICAgICAgfSBlbHNlIGlmICghbmV3U3JjLm1hdGNoKERFVl9NT0RFX0NPREVfUkVHRVhQKSkge1xuICAgICAgICAgICAgY29uc29sZS5lcnJvcignTmV3IGNvbW1lbnQgZmFpbHMgdG8gbWF0Y2ggb3JpZ2luYWwgcmVnZXhwJyk7XG4gICAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAgIHJldHVybiB7IGNvZGU6IG5ld1NyYyB9O1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgfVxuXG4gICAgICByZXR1cm4geyBjb2RlOiBzcmMgfTtcbiAgICB9XG4gIH07XG59XG5cbmV4cG9ydCBjb25zdCB2YWFkaW5Db25maWc6IFVzZXJDb25maWdGbiA9IChlbnYpID0+IHtcbiAgY29uc3QgZGV2TW9kZSA9IGVudi5tb2RlID09PSAnZGV2ZWxvcG1lbnQnO1xuICBjb25zdCBwcm9kdWN0aW9uTW9kZSA9ICFkZXZNb2RlICYmICFkZXZCdW5kbGVcblxuICBpZiAoZGV2TW9kZSAmJiBwcm9jZXNzLmVudi53YXRjaERvZ1BvcnQpIHtcbiAgICAvLyBPcGVuIGEgY29ubmVjdGlvbiB3aXRoIHRoZSBKYXZhIGRldi1tb2RlIGhhbmRsZXIgaW4gb3JkZXIgdG8gZmluaXNoXG4gICAgLy8gdml0ZSB3aGVuIGl0IGV4aXRzIG9yIGNyYXNoZXMuXG4gICAgcnVuV2F0Y2hEb2cocHJvY2Vzcy5lbnYud2F0Y2hEb2dQb3J0LCBwcm9jZXNzLmVudi53YXRjaERvZ0hvc3QpO1xuICB9XG5cbiAgcmV0dXJuIHtcbiAgICByb290OiBmcm9udGVuZEZvbGRlcixcbiAgICBiYXNlOiAnJyxcbiAgICBwdWJsaWNEaXI6IGZhbHNlLFxuICAgIHJlc29sdmU6IHtcbiAgICAgIGFsaWFzOiB7XG4gICAgICAgICdAdmFhZGluL2Zsb3ctZnJvbnRlbmQnOiBqYXJSZXNvdXJjZXNGb2xkZXIsXG4gICAgICAgIEZyb250ZW5kOiBmcm9udGVuZEZvbGRlclxuICAgICAgfSxcbiAgICAgIHByZXNlcnZlU3ltbGlua3M6IHRydWVcbiAgICB9LFxuICAgIGRlZmluZToge1xuICAgICAgT0ZGTElORV9QQVRIOiBzZXR0aW5ncy5vZmZsaW5lUGF0aCxcbiAgICAgIFZJVEVfRU5BQkxFRDogJ3RydWUnXG4gICAgfSxcbiAgICBzZXJ2ZXI6IHtcbiAgICAgIGhvc3Q6ICcxMjcuMC4wLjEnLFxuICAgICAgc3RyaWN0UG9ydDogdHJ1ZSxcbiAgICAgIGZzOiB7XG4gICAgICAgIGFsbG93OiBhbGxvd2VkRnJvbnRlbmRGb2xkZXJzXG4gICAgICB9XG4gICAgfSxcbiAgICBidWlsZDoge1xuICAgICAgb3V0RGlyOiBidWlsZE91dHB1dEZvbGRlcixcbiAgICAgIGVtcHR5T3V0RGlyOiBkZXZCdW5kbGUsXG4gICAgICBhc3NldHNEaXI6ICdWQUFESU4vYnVpbGQnLFxuICAgICAgcm9sbHVwT3B0aW9uczoge1xuICAgICAgICBpbnB1dDoge1xuICAgICAgICAgIGluZGV4aHRtbDogcHJvamVjdEluZGV4SHRtbCxcblxuICAgICAgICAgIC4uLihoYXNFeHBvcnRlZFdlYkNvbXBvbmVudHMgPyB7IHdlYmNvbXBvbmVudGh0bWw6IHBhdGgucmVzb2x2ZShmcm9udGVuZEZvbGRlciwgJ3dlYi1jb21wb25lbnQuaHRtbCcpIH0gOiB7fSlcbiAgICAgICAgfSxcbiAgICAgICAgb253YXJuOiAod2FybmluZzogcm9sbHVwLlJvbGx1cFdhcm5pbmcsIGRlZmF1bHRIYW5kbGVyOiByb2xsdXAuV2FybmluZ0hhbmRsZXIpID0+IHtcbiAgICAgICAgICBjb25zdCBpZ25vcmVFdmFsV2FybmluZyA9IFtcbiAgICAgICAgICAgICdnZW5lcmF0ZWQvamFyLXJlc291cmNlcy9GbG93Q2xpZW50LmpzJyxcbiAgICAgICAgICAgICdnZW5lcmF0ZWQvamFyLXJlc291cmNlcy92YWFkaW4tc3ByZWFkc2hlZXQvc3ByZWFkc2hlZXQtZXhwb3J0LmpzJyxcbiAgICAgICAgICAgICdAdmFhZGluL2NoYXJ0cy9zcmMvaGVscGVycy5qcydcbiAgICAgICAgICBdO1xuICAgICAgICAgIGlmICh3YXJuaW5nLmNvZGUgPT09ICdFVkFMJyAmJiB3YXJuaW5nLmlkICYmICEhaWdub3JlRXZhbFdhcm5pbmcuZmluZCgoaWQpID0+IHdhcm5pbmcuaWQuZW5kc1dpdGgoaWQpKSkge1xuICAgICAgICAgICAgcmV0dXJuO1xuICAgICAgICAgIH1cbiAgICAgICAgICBkZWZhdWx0SGFuZGxlcih3YXJuaW5nKTtcbiAgICAgICAgfVxuICAgICAgfVxuICAgIH0sXG4gICAgb3B0aW1pemVEZXBzOiB7XG4gICAgICBlbnRyaWVzOiBbXG4gICAgICAgIC8vIFByZS1zY2FuIGVudHJ5cG9pbnRzIGluIFZpdGUgdG8gYXZvaWQgcmVsb2FkaW5nIG9uIGZpcnN0IG9wZW5cbiAgICAgICAgJ2dlbmVyYXRlZC92YWFkaW4udHMnXG4gICAgICBdLFxuICAgICAgZXhjbHVkZTogW1xuICAgICAgICAnQHZhYWRpbi9yb3V0ZXInLFxuICAgICAgICAnQHZhYWRpbi92YWFkaW4tbGljZW5zZS1jaGVja2VyJyxcbiAgICAgICAgJ0B2YWFkaW4vdmFhZGluLXVzYWdlLXN0YXRpc3RpY3MnLFxuICAgICAgICAnd29ya2JveC1jb3JlJyxcbiAgICAgICAgJ3dvcmtib3gtcHJlY2FjaGluZycsXG4gICAgICAgICd3b3JrYm94LXJvdXRpbmcnLFxuICAgICAgICAnd29ya2JveC1zdHJhdGVnaWVzJ1xuICAgICAgXVxuICAgIH0sXG4gICAgcGx1Z2luczogW1xuICAgICAgcHJvZHVjdGlvbk1vZGUgJiYgYnJvdGxpKCksXG4gICAgICBkZXZNb2RlICYmIHZhYWRpbkJ1bmRsZXNQbHVnaW4oKSxcbiAgICAgIGRldk1vZGUgJiYgc2hvd1JlY29tcGlsZVJlYXNvbigpLFxuICAgICAgc2V0dGluZ3Mub2ZmbGluZUVuYWJsZWQgJiYgYnVpbGRTV1BsdWdpbih7IGRldk1vZGUgfSksXG4gICAgICAhZGV2TW9kZSAmJiBzdGF0c0V4dHJhY3RlclBsdWdpbigpLFxuICAgICAgZGV2QnVuZGxlICYmIHByZXNlcnZlVXNhZ2VTdGF0cygpLFxuICAgICAgdGhlbWVQbHVnaW4oeyBkZXZNb2RlIH0pLFxuICAgICAgcG9zdGNzc0xpdCh7XG4gICAgICAgIGluY2x1ZGU6IFsnKiovKi5jc3MnLCAvLipcXC8uKlxcLmNzc1xcPy4qL10sXG4gICAgICAgIGV4Y2x1ZGU6IFtcbiAgICAgICAgICBgJHt0aGVtZUZvbGRlcn0vKiovKi5jc3NgLFxuICAgICAgICAgIG5ldyBSZWdFeHAoYCR7dGhlbWVGb2xkZXJ9Ly4qLy4qXFxcXC5jc3NcXFxcPy4qYCksXG4gICAgICAgICAgYCR7dGhlbWVSZXNvdXJjZUZvbGRlcn0vKiovKi5jc3NgLFxuICAgICAgICAgIG5ldyBSZWdFeHAoYCR7dGhlbWVSZXNvdXJjZUZvbGRlcn0vLiovLipcXFxcLmNzc1xcXFw/LipgKSxcbiAgICAgICAgICBuZXcgUmVnRXhwKCcuKi8uKlxcXFw/aHRtbC1wcm94eS4qJylcbiAgICAgICAgXVxuICAgICAgfSksXG4gICAgICB7XG4gICAgICAgIG5hbWU6ICd2YWFkaW46Zm9yY2UtcmVtb3ZlLWh0bWwtbWlkZGxld2FyZScsXG4gICAgICAgIHRyYW5zZm9ybUluZGV4SHRtbDoge1xuICAgICAgICAgIGVuZm9yY2U6ICdwcmUnLFxuICAgICAgICAgIHRyYW5zZm9ybShfaHRtbCwgeyBzZXJ2ZXIgfSkge1xuICAgICAgICAgICAgaWYgKHNlcnZlciAmJiAhc3BhTWlkZGxld2FyZUZvcmNlUmVtb3ZlZCkge1xuICAgICAgICAgICAgICBzZXJ2ZXIubWlkZGxld2FyZXMuc3RhY2sgPSBzZXJ2ZXIubWlkZGxld2FyZXMuc3RhY2suZmlsdGVyKChtdykgPT4ge1xuICAgICAgICAgICAgICAgIGNvbnN0IGhhbmRsZU5hbWUgPSAnJyArIG13LmhhbmRsZTtcbiAgICAgICAgICAgICAgICByZXR1cm4gIWhhbmRsZU5hbWUuaW5jbHVkZXMoJ3ZpdGVIdG1sRmFsbGJhY2tNaWRkbGV3YXJlJyk7XG4gICAgICAgICAgICAgIH0pO1xuICAgICAgICAgICAgICBzcGFNaWRkbGV3YXJlRm9yY2VSZW1vdmVkID0gdHJ1ZTtcbiAgICAgICAgICAgIH1cbiAgICAgICAgICB9XG4gICAgICAgIH1cbiAgICAgIH0sXG4gICAgICBoYXNFeHBvcnRlZFdlYkNvbXBvbmVudHMgJiYge1xuICAgICAgICBuYW1lOiAndmFhZGluOmluamVjdC1lbnRyeXBvaW50cy10by13ZWItY29tcG9uZW50LWh0bWwnLFxuICAgICAgICB0cmFuc2Zvcm1JbmRleEh0bWw6IHtcbiAgICAgICAgICBlbmZvcmNlOiAncHJlJyxcbiAgICAgICAgICB0cmFuc2Zvcm0oX2h0bWwsIHsgcGF0aCwgc2VydmVyIH0pIHtcbiAgICAgICAgICAgIGlmIChwYXRoICE9PSAnL3dlYi1jb21wb25lbnQuaHRtbCcpIHtcbiAgICAgICAgICAgICAgcmV0dXJuO1xuICAgICAgICAgICAgfVxuXG4gICAgICAgICAgICByZXR1cm4gW1xuICAgICAgICAgICAgICB7XG4gICAgICAgICAgICAgICAgdGFnOiAnc2NyaXB0JyxcbiAgICAgICAgICAgICAgICBhdHRyczogeyB0eXBlOiAnbW9kdWxlJywgc3JjOiBgL2dlbmVyYXRlZC92YWFkaW4td2ViLWNvbXBvbmVudC50c2AgfSxcbiAgICAgICAgICAgICAgICBpbmplY3RUbzogJ2hlYWQnXG4gICAgICAgICAgICAgIH1cbiAgICAgICAgICAgIF07XG4gICAgICAgICAgfVxuICAgICAgICB9XG4gICAgICB9LFxuICAgICAge1xuICAgICAgICBuYW1lOiAndmFhZGluOmluamVjdC1lbnRyeXBvaW50cy10by1pbmRleC1odG1sJyxcbiAgICAgICAgdHJhbnNmb3JtSW5kZXhIdG1sOiB7XG4gICAgICAgICAgZW5mb3JjZTogJ3ByZScsXG4gICAgICAgICAgdHJhbnNmb3JtKF9odG1sLCB7IHBhdGgsIHNlcnZlciB9KSB7XG4gICAgICAgICAgICBpZiAocGF0aCAhPT0gJy9pbmRleC5odG1sJykge1xuICAgICAgICAgICAgICByZXR1cm47XG4gICAgICAgICAgICB9XG5cbiAgICAgICAgICAgIGNvbnN0IHNjcmlwdHMgPSBbXTtcblxuICAgICAgICAgICAgaWYgKGRldk1vZGUpIHtcbiAgICAgICAgICAgICAgc2NyaXB0cy5wdXNoKHtcbiAgICAgICAgICAgICAgICB0YWc6ICdzY3JpcHQnLFxuICAgICAgICAgICAgICAgIGF0dHJzOiB7IHR5cGU6ICdtb2R1bGUnLCBzcmM6IGAvZ2VuZXJhdGVkL3ZpdGUtZGV2bW9kZS50c2AgfSxcbiAgICAgICAgICAgICAgICBpbmplY3RUbzogJ2hlYWQnXG4gICAgICAgICAgICAgIH0pO1xuICAgICAgICAgICAgfVxuICAgICAgICAgICAgc2NyaXB0cy5wdXNoKHtcbiAgICAgICAgICAgICAgdGFnOiAnc2NyaXB0JyxcbiAgICAgICAgICAgICAgYXR0cnM6IHsgdHlwZTogJ21vZHVsZScsIHNyYzogJy9nZW5lcmF0ZWQvdmFhZGluLnRzJyB9LFxuICAgICAgICAgICAgICBpbmplY3RUbzogJ2hlYWQnXG4gICAgICAgICAgICB9KTtcbiAgICAgICAgICAgIHJldHVybiBzY3JpcHRzO1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgfSxcbiAgICAgIGNoZWNrZXIoe1xuICAgICAgICB0eXBlc2NyaXB0OiB0cnVlXG4gICAgICB9KSxcbiAgICAgIHByb2R1Y3Rpb25Nb2RlICYmIHZpc3VhbGl6ZXIoeyBicm90bGlTaXplOiB0cnVlLCBmaWxlbmFtZTogYnVuZGxlU2l6ZUZpbGUgfSlcbiAgICBdXG4gIH07XG59O1xuXG5leHBvcnQgY29uc3Qgb3ZlcnJpZGVWYWFkaW5Db25maWcgPSAoY3VzdG9tQ29uZmlnOiBVc2VyQ29uZmlnRm4pID0+IHtcbiAgcmV0dXJuIGRlZmluZUNvbmZpZygoZW52KSA9PiBtZXJnZUNvbmZpZyh2YWFkaW5Db25maWcoZW52KSwgY3VzdG9tQ29uZmlnKGVudikpKTtcbn07XG5mdW5jdGlvbiBnZXRWZXJzaW9uKG1vZHVsZTogc3RyaW5nKTogc3RyaW5nIHtcbiAgY29uc3QgcGFja2FnZUpzb24gPSBwYXRoLnJlc29sdmUobm9kZU1vZHVsZXNGb2xkZXIsIG1vZHVsZSwgJ3BhY2thZ2UuanNvbicpO1xuICByZXR1cm4gSlNPTi5wYXJzZShyZWFkRmlsZVN5bmMocGFja2FnZUpzb24sIHsgZW5jb2Rpbmc6ICd1dGYtOCcgfSkpLnZlcnNpb247XG59XG5mdW5jdGlvbiBnZXRDdmRsTmFtZShtb2R1bGU6IHN0cmluZyk6IHN0cmluZyB7XG4gIGNvbnN0IHBhY2thZ2VKc29uID0gcGF0aC5yZXNvbHZlKG5vZGVNb2R1bGVzRm9sZGVyLCBtb2R1bGUsICdwYWNrYWdlLmpzb24nKTtcbiAgcmV0dXJuIEpTT04ucGFyc2UocmVhZEZpbGVTeW5jKHBhY2thZ2VKc29uLCB7IGVuY29kaW5nOiAndXRmLTgnIH0pKS5jdmRsTmFtZTtcbn1cbiIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiRjpcXFxcRWNsaXBzZSBXb3Jrc3BhY2VcXFxcbWVncGJyXFxcXHRhcmdldFxcXFxwbHVnaW5zXFxcXGFwcGxpY2F0aW9uLXRoZW1lLXBsdWdpblwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiRjpcXFxcRWNsaXBzZSBXb3Jrc3BhY2VcXFxcbWVncGJyXFxcXHRhcmdldFxcXFxwbHVnaW5zXFxcXGFwcGxpY2F0aW9uLXRoZW1lLXBsdWdpblxcXFx0aGVtZS1oYW5kbGUuanNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0Y6L0VjbGlwc2UlMjBXb3Jrc3BhY2UvbWVncGJyL3RhcmdldC9wbHVnaW5zL2FwcGxpY2F0aW9uLXRoZW1lLXBsdWdpbi90aGVtZS1oYW5kbGUuanNcIjsvKlxuICogQ29weXJpZ2h0IDIwMDAtMjAyMyBWYWFkaW4gTHRkLlxuICpcbiAqIExpY2Vuc2VkIHVuZGVyIHRoZSBBcGFjaGUgTGljZW5zZSwgVmVyc2lvbiAyLjAgKHRoZSBcIkxpY2Vuc2VcIik7IHlvdSBtYXkgbm90XG4gKiB1c2UgdGhpcyBmaWxlIGV4Y2VwdCBpbiBjb21wbGlhbmNlIHdpdGggdGhlIExpY2Vuc2UuIFlvdSBtYXkgb2J0YWluIGEgY29weSBvZlxuICogdGhlIExpY2Vuc2UgYXRcbiAqXG4gKiBodHRwOi8vd3d3LmFwYWNoZS5vcmcvbGljZW5zZXMvTElDRU5TRS0yLjBcbiAqXG4gKiBVbmxlc3MgcmVxdWlyZWQgYnkgYXBwbGljYWJsZSBsYXcgb3IgYWdyZWVkIHRvIGluIHdyaXRpbmcsIHNvZnR3YXJlXG4gKiBkaXN0cmlidXRlZCB1bmRlciB0aGUgTGljZW5zZSBpcyBkaXN0cmlidXRlZCBvbiBhbiBcIkFTIElTXCIgQkFTSVMsIFdJVEhPVVRcbiAqIFdBUlJBTlRJRVMgT1IgQ09ORElUSU9OUyBPRiBBTlkgS0lORCwgZWl0aGVyIGV4cHJlc3Mgb3IgaW1wbGllZC4gU2VlIHRoZVxuICogTGljZW5zZSBmb3IgdGhlIHNwZWNpZmljIGxhbmd1YWdlIGdvdmVybmluZyBwZXJtaXNzaW9ucyBhbmQgbGltaXRhdGlvbnMgdW5kZXJcbiAqIHRoZSBMaWNlbnNlLlxuICovXG5cbi8qKlxuICogVGhpcyBmaWxlIGNvbnRhaW5zIGZ1bmN0aW9ucyBmb3IgbG9vayB1cCBhbmQgaGFuZGxlIHRoZSB0aGVtZSByZXNvdXJjZXNcbiAqIGZvciBhcHBsaWNhdGlvbiB0aGVtZSBwbHVnaW4uXG4gKi9cbmltcG9ydCB7IGV4aXN0c1N5bmMsIHJlYWRGaWxlU3luYyB9IGZyb20gJ2ZzJztcbmltcG9ydCB7IHJlc29sdmUgfSBmcm9tICdwYXRoJztcbmltcG9ydCB7IHdyaXRlVGhlbWVGaWxlcyB9IGZyb20gJy4vdGhlbWUtZ2VuZXJhdG9yLmpzJztcbmltcG9ydCB7IGNvcHlTdGF0aWNBc3NldHMsIGNvcHlUaGVtZVJlc291cmNlcyB9IGZyb20gJy4vdGhlbWUtY29weS5qcyc7XG5cbi8vIG1hdGNoZXMgdGhlbWUgbmFtZSBpbiAnLi90aGVtZS1teS10aGVtZS5nZW5lcmF0ZWQuanMnXG5jb25zdCBuYW1lUmVnZXggPSAvdGhlbWUtKC4qKVxcLmdlbmVyYXRlZFxcLmpzLztcblxubGV0IHByZXZUaGVtZU5hbWUgPSB1bmRlZmluZWQ7XG5sZXQgZmlyc3RUaGVtZU5hbWUgPSB1bmRlZmluZWQ7XG5cbi8qKlxuICogTG9va3MgdXAgZm9yIGEgdGhlbWUgcmVzb3VyY2VzIGluIGEgY3VycmVudCBwcm9qZWN0IGFuZCBpbiBqYXIgZGVwZW5kZW5jaWVzLFxuICogY29waWVzIHRoZSBmb3VuZCByZXNvdXJjZXMgYW5kIGdlbmVyYXRlcy91cGRhdGVzIG1ldGEgZGF0YSBmb3Igd2VicGFja1xuICogY29tcGlsYXRpb24uXG4gKlxuICogQHBhcmFtIHtvYmplY3R9IG9wdGlvbnMgYXBwbGljYXRpb24gdGhlbWUgcGx1Z2luIG1hbmRhdG9yeSBvcHRpb25zLFxuICogQHNlZSB7QGxpbmsgQXBwbGljYXRpb25UaGVtZVBsdWdpbn1cbiAqXG4gKiBAcGFyYW0gbG9nZ2VyIGFwcGxpY2F0aW9uIHRoZW1lIHBsdWdpbiBsb2dnZXJcbiAqL1xuZnVuY3Rpb24gcHJvY2Vzc1RoZW1lUmVzb3VyY2VzKG9wdGlvbnMsIGxvZ2dlcikge1xuICBjb25zdCB0aGVtZU5hbWUgPSBleHRyYWN0VGhlbWVOYW1lKG9wdGlvbnMuZnJvbnRlbmRHZW5lcmF0ZWRGb2xkZXIpO1xuICBpZiAodGhlbWVOYW1lKSB7XG4gICAgaWYgKCFwcmV2VGhlbWVOYW1lICYmICFmaXJzdFRoZW1lTmFtZSkge1xuICAgICAgZmlyc3RUaGVtZU5hbWUgPSB0aGVtZU5hbWU7XG4gICAgfSBlbHNlIGlmIChcbiAgICAgIChwcmV2VGhlbWVOYW1lICYmIHByZXZUaGVtZU5hbWUgIT09IHRoZW1lTmFtZSAmJiBmaXJzdFRoZW1lTmFtZSAhPT0gdGhlbWVOYW1lKSB8fFxuICAgICAgKCFwcmV2VGhlbWVOYW1lICYmIGZpcnN0VGhlbWVOYW1lICE9PSB0aGVtZU5hbWUpXG4gICAgKSB7XG4gICAgICAvLyBXYXJuaW5nIG1lc3NhZ2UgaXMgc2hvd24gdG8gdGhlIGRldmVsb3BlciB3aGVuOlxuICAgICAgLy8gMS4gSGUgaXMgc3dpdGNoaW5nIHRvIGFueSB0aGVtZSwgd2hpY2ggaXMgZGlmZmVyIGZyb20gb25lIGJlaW5nIHNldCB1cFxuICAgICAgLy8gb24gYXBwbGljYXRpb24gc3RhcnR1cCwgYnkgY2hhbmdpbmcgdGhlbWUgbmFtZSBpbiBgQFRoZW1lKClgXG4gICAgICAvLyAyLiBIZSByZW1vdmVzIG9yIGNvbW1lbnRzIG91dCBgQFRoZW1lKClgIHRvIHNlZSBob3cgdGhlIGFwcFxuICAgICAgLy8gbG9va3MgbGlrZSB3aXRob3V0IHRoZW1pbmcsIGFuZCB0aGVuIGFnYWluIGJyaW5ncyBgQFRoZW1lKClgIGJhY2tcbiAgICAgIC8vIHdpdGggYSB0aGVtZU5hbWUgd2hpY2ggaXMgZGlmZmVyIGZyb20gb25lIGJlaW5nIHNldCB1cCBvbiBhcHBsaWNhdGlvblxuICAgICAgLy8gc3RhcnR1cC5cbiAgICAgIGNvbnN0IHdhcm5pbmcgPSBgQXR0ZW50aW9uOiBBY3RpdmUgdGhlbWUgaXMgc3dpdGNoZWQgdG8gJyR7dGhlbWVOYW1lfScuYDtcbiAgICAgIGNvbnN0IGRlc2NyaXB0aW9uID0gYFxuICAgICAgTm90ZSB0aGF0IGFkZGluZyBuZXcgc3R5bGUgc2hlZXQgZmlsZXMgdG8gJy90aGVtZXMvJHt0aGVtZU5hbWV9L2NvbXBvbmVudHMnLCBcbiAgICAgIG1heSBub3QgYmUgdGFrZW4gaW50byBlZmZlY3QgdW50aWwgdGhlIG5leHQgYXBwbGljYXRpb24gcmVzdGFydC5cbiAgICAgIENoYW5nZXMgdG8gYWxyZWFkeSBleGlzdGluZyBzdHlsZSBzaGVldCBmaWxlcyBhcmUgYmVpbmcgcmVsb2FkZWQgYXMgYmVmb3JlLmA7XG4gICAgICBsb2dnZXIud2FybignKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKicpO1xuICAgICAgbG9nZ2VyLndhcm4od2FybmluZyk7XG4gICAgICBsb2dnZXIud2FybihkZXNjcmlwdGlvbik7XG4gICAgICBsb2dnZXIud2FybignKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKicpO1xuICAgIH1cbiAgICBwcmV2VGhlbWVOYW1lID0gdGhlbWVOYW1lO1xuXG4gICAgZmluZFRoZW1lRm9sZGVyQW5kSGFuZGxlVGhlbWUodGhlbWVOYW1lLCBvcHRpb25zLCBsb2dnZXIpO1xuICB9IGVsc2Uge1xuICAgIC8vIFRoaXMgaXMgbmVlZGVkIGluIHRoZSBzaXR1YXRpb24gdGhhdCB0aGUgdXNlciBkZWNpZGVzIHRvIGNvbW1lbnQgb3JcbiAgICAvLyByZW1vdmUgdGhlIEBUaGVtZSguLi4pIGNvbXBsZXRlbHkgdG8gc2VlIGhvdyB0aGUgYXBwbGljYXRpb24gbG9va3NcbiAgICAvLyB3aXRob3V0IGFueSB0aGVtZS4gVGhlbiB3aGVuIHRoZSB1c2VyIGJyaW5ncyBiYWNrIG9uZSBvZiB0aGUgdGhlbWVzLFxuICAgIC8vIHRoZSBwcmV2aW91cyB0aGVtZSBzaG91bGQgYmUgdW5kZWZpbmVkIHRvIGVuYWJsZSB1cyB0byBkZXRlY3QgdGhlIGNoYW5nZS5cbiAgICBwcmV2VGhlbWVOYW1lID0gdW5kZWZpbmVkO1xuICAgIGxvZ2dlci5kZWJ1ZygnU2tpcHBpbmcgVmFhZGluIGFwcGxpY2F0aW9uIHRoZW1lIGhhbmRsaW5nLicpO1xuICAgIGxvZ2dlci50cmFjZSgnTW9zdCBsaWtlbHkgbm8gQFRoZW1lIGFubm90YXRpb24gZm9yIGFwcGxpY2F0aW9uIG9yIG9ubHkgdGhlbWVDbGFzcyB1c2VkLicpO1xuICB9XG59XG5cbi8qKlxuICogU2VhcmNoIGZvciB0aGUgZ2l2ZW4gdGhlbWUgaW4gdGhlIHByb2plY3QgYW5kIHJlc291cmNlIGZvbGRlcnMuXG4gKlxuICogQHBhcmFtIHtzdHJpbmd9IHRoZW1lTmFtZSBuYW1lIG9mIHRoZW1lIHRvIGZpbmRcbiAqIEBwYXJhbSB7b2JqZWN0fSBvcHRpb25zIGFwcGxpY2F0aW9uIHRoZW1lIHBsdWdpbiBtYW5kYXRvcnkgb3B0aW9ucyxcbiAqIEBzZWUge0BsaW5rIEFwcGxpY2F0aW9uVGhlbWVQbHVnaW59XG4gKiBAcGFyYW0gbG9nZ2VyIGFwcGxpY2F0aW9uIHRoZW1lIHBsdWdpbiBsb2dnZXJcbiAqIEByZXR1cm4gdHJ1ZSBvciBmYWxzZSBmb3IgaWYgdGhlbWUgd2FzIGZvdW5kXG4gKi9cbmZ1bmN0aW9uIGZpbmRUaGVtZUZvbGRlckFuZEhhbmRsZVRoZW1lKHRoZW1lTmFtZSwgb3B0aW9ucywgbG9nZ2VyKSB7XG4gIGxldCB0aGVtZUZvdW5kID0gZmFsc2U7XG4gIGZvciAobGV0IGkgPSAwOyBpIDwgb3B0aW9ucy50aGVtZVByb2plY3RGb2xkZXJzLmxlbmd0aDsgaSsrKSB7XG4gICAgY29uc3QgdGhlbWVQcm9qZWN0Rm9sZGVyID0gb3B0aW9ucy50aGVtZVByb2plY3RGb2xkZXJzW2ldO1xuICAgIGlmIChleGlzdHNTeW5jKHRoZW1lUHJvamVjdEZvbGRlcikpIHtcbiAgICAgIGxvZ2dlci5kZWJ1ZyhcIlNlYXJjaGluZyB0aGVtZXMgZm9sZGVyICdcIiArIHRoZW1lUHJvamVjdEZvbGRlciArIFwiJyBmb3IgdGhlbWUgJ1wiICsgdGhlbWVOYW1lICsgXCInXCIpO1xuICAgICAgY29uc3QgaGFuZGxlZCA9IGhhbmRsZVRoZW1lcyh0aGVtZU5hbWUsIHRoZW1lUHJvamVjdEZvbGRlciwgb3B0aW9ucywgbG9nZ2VyKTtcbiAgICAgIGlmIChoYW5kbGVkKSB7XG4gICAgICAgIGlmICh0aGVtZUZvdW5kKSB7XG4gICAgICAgICAgdGhyb3cgbmV3IEVycm9yKFxuICAgICAgICAgICAgXCJGb3VuZCB0aGVtZSBmaWxlcyBpbiAnXCIgK1xuICAgICAgICAgICAgICB0aGVtZVByb2plY3RGb2xkZXIgK1xuICAgICAgICAgICAgICBcIicgYW5kICdcIiArXG4gICAgICAgICAgICAgIHRoZW1lRm91bmQgK1xuICAgICAgICAgICAgICBcIicuIFRoZW1lIHNob3VsZCBvbmx5IGJlIGF2YWlsYWJsZSBpbiBvbmUgZm9sZGVyXCJcbiAgICAgICAgICApO1xuICAgICAgICB9XG4gICAgICAgIGxvZ2dlci5kZWJ1ZyhcIkZvdW5kIHRoZW1lIGZpbGVzIGZyb20gJ1wiICsgdGhlbWVQcm9qZWN0Rm9sZGVyICsgXCInXCIpO1xuICAgICAgICB0aGVtZUZvdW5kID0gdGhlbWVQcm9qZWN0Rm9sZGVyO1xuICAgICAgfVxuICAgIH1cbiAgfVxuXG4gIGlmIChleGlzdHNTeW5jKG9wdGlvbnMudGhlbWVSZXNvdXJjZUZvbGRlcikpIHtcbiAgICBpZiAodGhlbWVGb3VuZCAmJiBleGlzdHNTeW5jKHJlc29sdmUob3B0aW9ucy50aGVtZVJlc291cmNlRm9sZGVyLCB0aGVtZU5hbWUpKSkge1xuICAgICAgdGhyb3cgbmV3IEVycm9yKFxuICAgICAgICBcIlRoZW1lICdcIiArXG4gICAgICAgICAgdGhlbWVOYW1lICtcbiAgICAgICAgICBcIidzaG91bGQgbm90IGV4aXN0IGluc2lkZSBhIGphciBhbmQgaW4gdGhlIHByb2plY3QgYXQgdGhlIHNhbWUgdGltZVxcblwiICtcbiAgICAgICAgICAnRXh0ZW5kaW5nIGFub3RoZXIgdGhlbWUgaXMgcG9zc2libGUgYnkgYWRkaW5nIHsgXCJwYXJlbnRcIjogXCJteS1wYXJlbnQtdGhlbWVcIiB9IGVudHJ5IHRvIHRoZSB0aGVtZS5qc29uIGZpbGUgaW5zaWRlIHlvdXIgdGhlbWUgZm9sZGVyLidcbiAgICAgICk7XG4gICAgfVxuICAgIGxvZ2dlci5kZWJ1ZyhcbiAgICAgIFwiU2VhcmNoaW5nIHRoZW1lIGphciByZXNvdXJjZSBmb2xkZXIgJ1wiICsgb3B0aW9ucy50aGVtZVJlc291cmNlRm9sZGVyICsgXCInIGZvciB0aGVtZSAnXCIgKyB0aGVtZU5hbWUgKyBcIidcIlxuICAgICk7XG4gICAgaGFuZGxlVGhlbWVzKHRoZW1lTmFtZSwgb3B0aW9ucy50aGVtZVJlc291cmNlRm9sZGVyLCBvcHRpb25zLCBsb2dnZXIpO1xuICAgIHRoZW1lRm91bmQgPSB0cnVlO1xuICB9XG4gIHJldHVybiB0aGVtZUZvdW5kO1xufVxuXG4vKipcbiAqIENvcGllcyBzdGF0aWMgcmVzb3VyY2VzIGZvciB0aGVtZSBhbmQgZ2VuZXJhdGVzL3dyaXRlcyB0aGVcbiAqIFt0aGVtZS1uYW1lXS5nZW5lcmF0ZWQuanMgZm9yIHdlYnBhY2sgdG8gaGFuZGxlLlxuICpcbiAqIE5vdGUhIElmIGEgcGFyZW50IHRoZW1lIGlzIGRlZmluZWQgaXQgd2lsbCBhbHNvIGJlIGhhbmRsZWQgaGVyZSBzbyB0aGF0IHRoZSBwYXJlbnQgdGhlbWUgZ2VuZXJhdGVkIGZpbGUgaXNcbiAqIGdlbmVyYXRlZCBpbiBhZHZhbmNlIG9mIHRoZSB0aGVtZSBnZW5lcmF0ZWQgZmlsZS5cbiAqXG4gKiBAcGFyYW0ge3N0cmluZ30gdGhlbWVOYW1lIG5hbWUgb2YgdGhlbWUgdG8gaGFuZGxlXG4gKiBAcGFyYW0ge3N0cmluZ30gdGhlbWVzRm9sZGVyIGZvbGRlciBjb250YWluaW5nIGFwcGxpY2F0aW9uIHRoZW1lIGZvbGRlcnNcbiAqIEBwYXJhbSB7b2JqZWN0fSBvcHRpb25zIGFwcGxpY2F0aW9uIHRoZW1lIHBsdWdpbiBtYW5kYXRvcnkgb3B0aW9ucyxcbiAqIEBzZWUge0BsaW5rIEFwcGxpY2F0aW9uVGhlbWVQbHVnaW59XG4gKiBAcGFyYW0ge29iamVjdH0gbG9nZ2VyIHBsdWdpbiBsb2dnZXIgaW5zdGFuY2VcbiAqXG4gKiBAdGhyb3dzIEVycm9yIGlmIHBhcmVudCB0aGVtZSBkZWZpbmVkLCBidXQgY2FuJ3QgbG9jYXRlIHBhcmVudCB0aGVtZVxuICpcbiAqIEByZXR1cm5zIHRydWUgaWYgdGhlbWUgd2FzIGZvdW5kIGVsc2UgZmFsc2UuXG4gKi9cbmZ1bmN0aW9uIGhhbmRsZVRoZW1lcyh0aGVtZU5hbWUsIHRoZW1lc0ZvbGRlciwgb3B0aW9ucywgbG9nZ2VyKSB7XG4gIGNvbnN0IHRoZW1lRm9sZGVyID0gcmVzb2x2ZSh0aGVtZXNGb2xkZXIsIHRoZW1lTmFtZSk7XG4gIGlmIChleGlzdHNTeW5jKHRoZW1lRm9sZGVyKSkge1xuICAgIGxvZ2dlci5kZWJ1ZygnRm91bmQgdGhlbWUgJywgdGhlbWVOYW1lLCAnIGluIGZvbGRlciAnLCB0aGVtZUZvbGRlcik7XG5cbiAgICBjb25zdCB0aGVtZVByb3BlcnRpZXMgPSBnZXRUaGVtZVByb3BlcnRpZXModGhlbWVGb2xkZXIpO1xuXG4gICAgLy8gSWYgdGhlbWUgaGFzIHBhcmVudCBoYW5kbGUgcGFyZW50IHRoZW1lIGltbWVkaWF0ZWx5LlxuICAgIGlmICh0aGVtZVByb3BlcnRpZXMucGFyZW50KSB7XG4gICAgICBjb25zdCBmb3VuZCA9IGZpbmRUaGVtZUZvbGRlckFuZEhhbmRsZVRoZW1lKHRoZW1lUHJvcGVydGllcy5wYXJlbnQsIG9wdGlvbnMsIGxvZ2dlcik7XG4gICAgICBpZiAoIWZvdW5kKSB7XG4gICAgICAgIHRocm93IG5ldyBFcnJvcihcbiAgICAgICAgICBcIkNvdWxkIG5vdCBsb2NhdGUgZmlsZXMgZm9yIGRlZmluZWQgcGFyZW50IHRoZW1lICdcIiArXG4gICAgICAgICAgICB0aGVtZVByb3BlcnRpZXMucGFyZW50ICtcbiAgICAgICAgICAgIFwiJy5cXG5cIiArXG4gICAgICAgICAgICAnUGxlYXNlIHZlcmlmeSB0aGF0IGRlcGVuZGVuY3kgaXMgYWRkZWQgb3IgdGhlbWUgZm9sZGVyIGV4aXN0cy4nXG4gICAgICAgICk7XG4gICAgICB9XG4gICAgfVxuICAgIGNvcHlTdGF0aWNBc3NldHModGhlbWVOYW1lLCB0aGVtZVByb3BlcnRpZXMsIG9wdGlvbnMucHJvamVjdFN0YXRpY0Fzc2V0c091dHB1dEZvbGRlciwgbG9nZ2VyKTtcbiAgICBjb3B5VGhlbWVSZXNvdXJjZXModGhlbWVGb2xkZXIsIG9wdGlvbnMucHJvamVjdFN0YXRpY0Fzc2V0c091dHB1dEZvbGRlciwgbG9nZ2VyKTtcblxuICAgIHdyaXRlVGhlbWVGaWxlcyh0aGVtZUZvbGRlciwgdGhlbWVOYW1lLCB0aGVtZVByb3BlcnRpZXMsIG9wdGlvbnMpO1xuICAgIHJldHVybiB0cnVlO1xuICB9XG4gIHJldHVybiBmYWxzZTtcbn1cblxuZnVuY3Rpb24gZ2V0VGhlbWVQcm9wZXJ0aWVzKHRoZW1lRm9sZGVyKSB7XG4gIGNvbnN0IHRoZW1lUHJvcGVydHlGaWxlID0gcmVzb2x2ZSh0aGVtZUZvbGRlciwgJ3RoZW1lLmpzb24nKTtcbiAgaWYgKCFleGlzdHNTeW5jKHRoZW1lUHJvcGVydHlGaWxlKSkge1xuICAgIHJldHVybiB7fTtcbiAgfVxuICBjb25zdCB0aGVtZVByb3BlcnR5RmlsZUFzU3RyaW5nID0gcmVhZEZpbGVTeW5jKHRoZW1lUHJvcGVydHlGaWxlKTtcbiAgaWYgKHRoZW1lUHJvcGVydHlGaWxlQXNTdHJpbmcubGVuZ3RoID09PSAwKSB7XG4gICAgcmV0dXJuIHt9O1xuICB9XG4gIHJldHVybiBKU09OLnBhcnNlKHRoZW1lUHJvcGVydHlGaWxlQXNTdHJpbmcpO1xufVxuXG4vKipcbiAqIEV4dHJhY3RzIGN1cnJlbnQgdGhlbWUgbmFtZSBmcm9tIGF1dG8tZ2VuZXJhdGVkICd0aGVtZS5qcycgZmlsZSBsb2NhdGVkIG9uIGFcbiAqIGdpdmVuIGZvbGRlci5cbiAqIEBwYXJhbSBmcm9udGVuZEdlbmVyYXRlZEZvbGRlciBmb2xkZXIgaW4gcHJvamVjdCBjb250YWluaW5nICd0aGVtZS5qcycgZmlsZVxuICogQHJldHVybnMge3N0cmluZ30gY3VycmVudCB0aGVtZSBuYW1lXG4gKi9cbmZ1bmN0aW9uIGV4dHJhY3RUaGVtZU5hbWUoZnJvbnRlbmRHZW5lcmF0ZWRGb2xkZXIpIHtcbiAgaWYgKCFmcm9udGVuZEdlbmVyYXRlZEZvbGRlcikge1xuICAgIHRocm93IG5ldyBFcnJvcihcbiAgICAgIFwiQ291bGRuJ3QgZXh0cmFjdCB0aGVtZSBuYW1lIGZyb20gJ3RoZW1lLmpzJyxcIiArXG4gICAgICAgICcgYmVjYXVzZSB0aGUgcGF0aCB0byBmb2xkZXIgY29udGFpbmluZyB0aGlzIGZpbGUgaXMgZW1wdHkuIFBsZWFzZSBzZXQnICtcbiAgICAgICAgJyB0aGUgYSBjb3JyZWN0IGZvbGRlciBwYXRoIGluIEFwcGxpY2F0aW9uVGhlbWVQbHVnaW4gY29uc3RydWN0b3InICtcbiAgICAgICAgJyBwYXJhbWV0ZXJzLidcbiAgICApO1xuICB9XG4gIGNvbnN0IGdlbmVyYXRlZFRoZW1lRmlsZSA9IHJlc29sdmUoZnJvbnRlbmRHZW5lcmF0ZWRGb2xkZXIsICd0aGVtZS5qcycpO1xuICBpZiAoZXhpc3RzU3luYyhnZW5lcmF0ZWRUaGVtZUZpbGUpKSB7XG4gICAgLy8gcmVhZCB0aGVtZSBuYW1lIGZyb20gdGhlICdnZW5lcmF0ZWQvdGhlbWUuanMnIGFzIHRoZXJlIHdlIGFsd2F5c1xuICAgIC8vIG1hcmsgdGhlIHVzZWQgdGhlbWUgZm9yIHdlYnBhY2sgdG8gaGFuZGxlLlxuICAgIGNvbnN0IHRoZW1lTmFtZSA9IG5hbWVSZWdleC5leGVjKHJlYWRGaWxlU3luYyhnZW5lcmF0ZWRUaGVtZUZpbGUsIHsgZW5jb2Rpbmc6ICd1dGY4JyB9KSlbMV07XG4gICAgaWYgKCF0aGVtZU5hbWUpIHtcbiAgICAgIHRocm93IG5ldyBFcnJvcihcIkNvdWxkbid0IHBhcnNlIHRoZW1lIG5hbWUgZnJvbSAnXCIgKyBnZW5lcmF0ZWRUaGVtZUZpbGUgKyBcIicuXCIpO1xuICAgIH1cbiAgICByZXR1cm4gdGhlbWVOYW1lO1xuICB9IGVsc2Uge1xuICAgIHJldHVybiAnJztcbiAgfVxufVxuXG4vKipcbiAqIEZpbmRzIGFsbCB0aGUgcGFyZW50IHRoZW1lcyBsb2NhdGVkIGluIHRoZSBwcm9qZWN0IHRoZW1lcyBmb2xkZXJzIGFuZCBpblxuICogdGhlIEpBUiBkZXBlbmRlbmNpZXMgd2l0aCByZXNwZWN0IHRvIHRoZSBnaXZlbiBjdXN0b20gdGhlbWUgd2l0aFxuICoge0Bjb2RlIHRoZW1lTmFtZX0uXG4gKiBAcGFyYW0ge3N0cmluZ30gdGhlbWVOYW1lIGdpdmVuIGN1c3RvbSB0aGVtZSBuYW1lIHRvIGxvb2sgcGFyZW50cyBmb3JcbiAqIEBwYXJhbSB7b2JqZWN0fSBvcHRpb25zIGFwcGxpY2F0aW9uIHRoZW1lIHBsdWdpbiBtYW5kYXRvcnkgb3B0aW9ucyxcbiAqIEBzZWUge0BsaW5rIEFwcGxpY2F0aW9uVGhlbWVQbHVnaW59XG4gKiBAcmV0dXJucyB7c3RyaW5nW119IGFycmF5IG9mIHBhdGhzIHRvIGZvdW5kIHBhcmVudCB0aGVtZXMgd2l0aCByZXNwZWN0IHRvIHRoZVxuICogZ2l2ZW4gY3VzdG9tIHRoZW1lXG4gKi9cbmZ1bmN0aW9uIGZpbmRQYXJlbnRUaGVtZXModGhlbWVOYW1lLCBvcHRpb25zKSB7XG4gIGNvbnN0IGV4aXN0aW5nVGhlbWVGb2xkZXJzID0gW29wdGlvbnMudGhlbWVSZXNvdXJjZUZvbGRlciwgLi4ub3B0aW9ucy50aGVtZVByb2plY3RGb2xkZXJzXS5maWx0ZXIoKGZvbGRlcikgPT5cbiAgICBleGlzdHNTeW5jKGZvbGRlcilcbiAgKTtcbiAgcmV0dXJuIGNvbGxlY3RQYXJlbnRUaGVtZXModGhlbWVOYW1lLCBleGlzdGluZ1RoZW1lRm9sZGVycywgZmFsc2UpO1xufVxuXG5mdW5jdGlvbiBjb2xsZWN0UGFyZW50VGhlbWVzKHRoZW1lTmFtZSwgdGhlbWVGb2xkZXJzLCBpc1BhcmVudCkge1xuICBsZXQgZm91bmRQYXJlbnRUaGVtZXMgPSBbXTtcbiAgdGhlbWVGb2xkZXJzLmZvckVhY2goKGZvbGRlcikgPT4ge1xuICAgIGNvbnN0IHRoZW1lRm9sZGVyID0gcmVzb2x2ZShmb2xkZXIsIHRoZW1lTmFtZSk7XG4gICAgaWYgKGV4aXN0c1N5bmModGhlbWVGb2xkZXIpKSB7XG4gICAgICBjb25zdCB0aGVtZVByb3BlcnRpZXMgPSBnZXRUaGVtZVByb3BlcnRpZXModGhlbWVGb2xkZXIpO1xuXG4gICAgICBpZiAodGhlbWVQcm9wZXJ0aWVzLnBhcmVudCkge1xuICAgICAgICBmb3VuZFBhcmVudFRoZW1lcy5wdXNoKC4uLmNvbGxlY3RQYXJlbnRUaGVtZXModGhlbWVQcm9wZXJ0aWVzLnBhcmVudCwgdGhlbWVGb2xkZXJzLCB0cnVlKSk7XG4gICAgICAgIGlmICghZm91bmRQYXJlbnRUaGVtZXMubGVuZ3RoKSB7XG4gICAgICAgICAgdGhyb3cgbmV3IEVycm9yKFxuICAgICAgICAgICAgXCJDb3VsZCBub3QgbG9jYXRlIGZpbGVzIGZvciBkZWZpbmVkIHBhcmVudCB0aGVtZSAnXCIgK1xuICAgICAgICAgICAgICB0aGVtZVByb3BlcnRpZXMucGFyZW50ICtcbiAgICAgICAgICAgICAgXCInLlxcblwiICtcbiAgICAgICAgICAgICAgJ1BsZWFzZSB2ZXJpZnkgdGhhdCBkZXBlbmRlbmN5IGlzIGFkZGVkIG9yIHRoZW1lIGZvbGRlciBleGlzdHMuJ1xuICAgICAgICAgICk7XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICAgIC8vIEFkZCBhIHRoZW1lIHBhdGggdG8gcmVzdWx0IGNvbGxlY3Rpb24gb25seSBpZiBhIGdpdmVuIHRoZW1lTmFtZVxuICAgICAgLy8gaXMgc3VwcG9zZWQgdG8gYmUgYSBwYXJlbnQgdGhlbWVcbiAgICAgIGlmIChpc1BhcmVudCkge1xuICAgICAgICBmb3VuZFBhcmVudFRoZW1lcy5wdXNoKHRoZW1lRm9sZGVyKTtcbiAgICAgIH1cbiAgICB9XG4gIH0pO1xuICByZXR1cm4gZm91bmRQYXJlbnRUaGVtZXM7XG59XG5cbmV4cG9ydCB7IHByb2Nlc3NUaGVtZVJlc291cmNlcywgZXh0cmFjdFRoZW1lTmFtZSwgZmluZFBhcmVudFRoZW1lcyB9O1xuIiwgImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJGOlxcXFxFY2xpcHNlIFdvcmtzcGFjZVxcXFxtZWdwYnJcXFxcdGFyZ2V0XFxcXHBsdWdpbnNcXFxcYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJGOlxcXFxFY2xpcHNlIFdvcmtzcGFjZVxcXFxtZWdwYnJcXFxcdGFyZ2V0XFxcXHBsdWdpbnNcXFxcYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luXFxcXHRoZW1lLWdlbmVyYXRvci5qc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vRjovRWNsaXBzZSUyMFdvcmtzcGFjZS9tZWdwYnIvdGFyZ2V0L3BsdWdpbnMvYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luL3RoZW1lLWdlbmVyYXRvci5qc1wiOy8qXG4gKiBDb3B5cmlnaHQgMjAwMC0yMDIzIFZhYWRpbiBMdGQuXG4gKlxuICogTGljZW5zZWQgdW5kZXIgdGhlIEFwYWNoZSBMaWNlbnNlLCBWZXJzaW9uIDIuMCAodGhlIFwiTGljZW5zZVwiKTsgeW91IG1heSBub3RcbiAqIHVzZSB0aGlzIGZpbGUgZXhjZXB0IGluIGNvbXBsaWFuY2Ugd2l0aCB0aGUgTGljZW5zZS4gWW91IG1heSBvYnRhaW4gYSBjb3B5IG9mXG4gKiB0aGUgTGljZW5zZSBhdFxuICpcbiAqIGh0dHA6Ly93d3cuYXBhY2hlLm9yZy9saWNlbnNlcy9MSUNFTlNFLTIuMFxuICpcbiAqIFVubGVzcyByZXF1aXJlZCBieSBhcHBsaWNhYmxlIGxhdyBvciBhZ3JlZWQgdG8gaW4gd3JpdGluZywgc29mdHdhcmVcbiAqIGRpc3RyaWJ1dGVkIHVuZGVyIHRoZSBMaWNlbnNlIGlzIGRpc3RyaWJ1dGVkIG9uIGFuIFwiQVMgSVNcIiBCQVNJUywgV0lUSE9VVFxuICogV0FSUkFOVElFUyBPUiBDT05ESVRJT05TIE9GIEFOWSBLSU5ELCBlaXRoZXIgZXhwcmVzcyBvciBpbXBsaWVkLiBTZWUgdGhlXG4gKiBMaWNlbnNlIGZvciB0aGUgc3BlY2lmaWMgbGFuZ3VhZ2UgZ292ZXJuaW5nIHBlcm1pc3Npb25zIGFuZCBsaW1pdGF0aW9ucyB1bmRlclxuICogdGhlIExpY2Vuc2UuXG4gKi9cblxuLyoqXG4gKiBUaGlzIGZpbGUgaGFuZGxlcyB0aGUgZ2VuZXJhdGlvbiBvZiB0aGUgJ1t0aGVtZS1uYW1lXS5qcycgdG9cbiAqIHRoZSB0aGVtZXMvW3RoZW1lLW5hbWVdIGZvbGRlciBhY2NvcmRpbmcgdG8gcHJvcGVydGllcyBmcm9tICd0aGVtZS5qc29uJy5cbiAqL1xuaW1wb3J0IHsgZ2xvYlN5bmMgfSBmcm9tICdnbG9iJztcbmltcG9ydCB7IHJlc29sdmUsIGJhc2VuYW1lIH0gZnJvbSAncGF0aCc7XG5pbXBvcnQgeyBleGlzdHNTeW5jLCByZWFkRmlsZVN5bmMsIHdyaXRlRmlsZVN5bmMgfSBmcm9tICdmcyc7XG5pbXBvcnQgeyBjaGVja01vZHVsZXMgfSBmcm9tICcuL3RoZW1lLWNvcHkuanMnO1xuXG4vLyBTcGVjaWFsIGZvbGRlciBpbnNpZGUgYSB0aGVtZSBmb3IgY29tcG9uZW50IHRoZW1lcyB0aGF0IGdvIGluc2lkZSB0aGUgY29tcG9uZW50IHNoYWRvdyByb290XG5jb25zdCB0aGVtZUNvbXBvbmVudHNGb2xkZXIgPSAnY29tcG9uZW50cyc7XG4vLyBUaGUgY29udGVudHMgb2YgYSBnbG9iYWwgQ1NTIGZpbGUgd2l0aCB0aGlzIG5hbWUgaW4gYSB0aGVtZSBpcyBhbHdheXMgYWRkZWQgdG9cbi8vIHRoZSBkb2N1bWVudC4gRS5nLiBAZm9udC1mYWNlIG11c3QgYmUgaW4gdGhpc1xuY29uc3QgZG9jdW1lbnRDc3NGaWxlbmFtZSA9ICdkb2N1bWVudC5jc3MnO1xuLy8gc3R5bGVzLmNzcyBpcyB0aGUgb25seSBlbnRyeXBvaW50IGNzcyBmaWxlIHdpdGggZG9jdW1lbnQuY3NzLiBFdmVyeXRoaW5nIGVsc2Ugc2hvdWxkIGJlIGltcG9ydGVkIHVzaW5nIGNzcyBAaW1wb3J0XG5jb25zdCBzdHlsZXNDc3NGaWxlbmFtZSA9ICdzdHlsZXMuY3NzJztcblxuY29uc3QgQ1NTSU1QT1JUX0NPTU1FTlQgPSAnQ1NTSW1wb3J0IGVuZCc7XG5jb25zdCBoZWFkZXJJbXBvcnQgPSBgaW1wb3J0ICdjb25zdHJ1Y3Qtc3R5bGUtc2hlZXRzLXBvbHlmaWxsJztcbmA7XG5cbi8qKlxuICogR2VuZXJhdGUgdGhlIFt0aGVtZU5hbWVdLmpzIGZpbGUgZm9yIHRoZW1lRm9sZGVyIHdoaWNoIGNvbGxlY3RzIGFsbCByZXF1aXJlZCBpbmZvcm1hdGlvbiBmcm9tIHRoZSBmb2xkZXIuXG4gKlxuICogQHBhcmFtIHtzdHJpbmd9IHRoZW1lRm9sZGVyIGZvbGRlciBvZiB0aGUgdGhlbWVcbiAqIEBwYXJhbSB7c3RyaW5nfSB0aGVtZU5hbWUgbmFtZSBvZiB0aGUgaGFuZGxlZCB0aGVtZVxuICogQHBhcmFtIHtKU09OfSB0aGVtZVByb3BlcnRpZXMgY29udGVudCBvZiB0aGVtZS5qc29uXG4gKiBAcGFyYW0ge09iamVjdH0gb3B0aW9ucyBidWlsZCBvcHRpb25zIChlLmcuIHByb2Qgb3IgZGV2IG1vZGUpXG4gKiBAcmV0dXJucyB7c3RyaW5nfSB0aGVtZSBmaWxlIGNvbnRlbnRcbiAqL1xuZnVuY3Rpb24gd3JpdGVUaGVtZUZpbGVzKHRoZW1lRm9sZGVyLCB0aGVtZU5hbWUsIHRoZW1lUHJvcGVydGllcywgb3B0aW9ucykge1xuICBjb25zdCBwcm9kdWN0aW9uTW9kZSA9ICFvcHRpb25zLmRldk1vZGU7XG4gIGNvbnN0IHVzZURldlNlcnZlck9ySW5Qcm9kdWN0aW9uTW9kZSA9ICFvcHRpb25zLnVzZURldkJ1bmRsZTtcbiAgY29uc3Qgb3V0cHV0Rm9sZGVyID0gb3B0aW9ucy5mcm9udGVuZEdlbmVyYXRlZEZvbGRlcjtcbiAgY29uc3Qgc3R5bGVzID0gcmVzb2x2ZSh0aGVtZUZvbGRlciwgc3R5bGVzQ3NzRmlsZW5hbWUpO1xuICBjb25zdCBkb2N1bWVudENzc0ZpbGUgPSByZXNvbHZlKHRoZW1lRm9sZGVyLCBkb2N1bWVudENzc0ZpbGVuYW1lKTtcbiAgY29uc3QgYXV0b0luamVjdENvbXBvbmVudHMgPSB0aGVtZVByb3BlcnRpZXMuYXV0b0luamVjdENvbXBvbmVudHMgPz8gdHJ1ZTtcbiAgY29uc3QgZ2xvYmFsRmlsZW5hbWUgPSAndGhlbWUtJyArIHRoZW1lTmFtZSArICcuZ2xvYmFsLmdlbmVyYXRlZC5qcyc7XG4gIGNvbnN0IGNvbXBvbmVudHNGaWxlbmFtZSA9ICd0aGVtZS0nICsgdGhlbWVOYW1lICsgJy5jb21wb25lbnRzLmdlbmVyYXRlZC5qcyc7XG4gIGNvbnN0IHRoZW1lRmlsZW5hbWUgPSAndGhlbWUtJyArIHRoZW1lTmFtZSArICcuZ2VuZXJhdGVkLmpzJztcblxuICBsZXQgdGhlbWVGaWxlQ29udGVudCA9IGhlYWRlckltcG9ydDtcbiAgbGV0IGdsb2JhbEltcG9ydENvbnRlbnQgPSAnLy8gV2hlbiB0aGlzIGZpbGUgaXMgaW1wb3J0ZWQsIGdsb2JhbCBzdHlsZXMgYXJlIGF1dG9tYXRpY2FsbHkgYXBwbGllZFxcbic7XG4gIGxldCBjb21wb25lbnRzRmlsZUNvbnRlbnQgPSAnJztcbiAgdmFyIGNvbXBvbmVudHNGaWxlcztcblxuICBpZiAoYXV0b0luamVjdENvbXBvbmVudHMpIHtcbiAgICBjb21wb25lbnRzRmlsZXMgPSBnbG9iU3luYygnKi5jc3MnLCB7XG4gICAgICBjd2Q6IHJlc29sdmUodGhlbWVGb2xkZXIsIHRoZW1lQ29tcG9uZW50c0ZvbGRlciksXG4gICAgICBub2RpcjogdHJ1ZVxuICAgIH0pO1xuXG4gICAgaWYgKGNvbXBvbmVudHNGaWxlcy5sZW5ndGggPiAwKSB7XG4gICAgICBjb21wb25lbnRzRmlsZUNvbnRlbnQgKz1cbiAgICAgICAgXCJpbXBvcnQgeyB1bnNhZmVDU1MsIHJlZ2lzdGVyU3R5bGVzIH0gZnJvbSAnQHZhYWRpbi92YWFkaW4tdGhlbWFibGUtbWl4aW4vcmVnaXN0ZXItc3R5bGVzJztcXG5cIjtcbiAgICB9XG4gIH1cblxuICBpZiAodGhlbWVQcm9wZXJ0aWVzLnBhcmVudCkge1xuICAgIHRoZW1lRmlsZUNvbnRlbnQgKz0gYGltcG9ydCB7IGFwcGx5VGhlbWUgYXMgYXBwbHlCYXNlVGhlbWUgfSBmcm9tICcuL3RoZW1lLSR7dGhlbWVQcm9wZXJ0aWVzLnBhcmVudH0uZ2VuZXJhdGVkLmpzJztcXG5gO1xuICB9XG5cbiAgdGhlbWVGaWxlQ29udGVudCArPSBgaW1wb3J0IHsgaW5qZWN0R2xvYmFsQ3NzIH0gZnJvbSAnRnJvbnRlbmQvZ2VuZXJhdGVkL2phci1yZXNvdXJjZXMvdGhlbWUtdXRpbC5qcyc7XFxuYDtcbiAgdGhlbWVGaWxlQ29udGVudCArPSBgaW1wb3J0ICcuLyR7Y29tcG9uZW50c0ZpbGVuYW1lfSc7XFxuYDtcblxuICB0aGVtZUZpbGVDb250ZW50ICs9IGBsZXQgbmVlZHNSZWxvYWRPbkNoYW5nZXMgPSBmYWxzZTtcXG5gO1xuICBjb25zdCBpbXBvcnRzID0gW107XG4gIGNvbnN0IGNvbXBvbmVudENzc0ltcG9ydHMgPSBbXTtcbiAgY29uc3QgZ2xvYmFsRmlsZUNvbnRlbnQgPSBbXTtcbiAgY29uc3QgZ2xvYmFsQ3NzQ29kZSA9IFtdO1xuICBjb25zdCBzaGFkb3dPbmx5Q3NzID0gW107XG4gIGNvbnN0IGNvbXBvbmVudENzc0NvZGUgPSBbXTtcbiAgY29uc3QgcGFyZW50VGhlbWUgPSB0aGVtZVByb3BlcnRpZXMucGFyZW50ID8gJ2FwcGx5QmFzZVRoZW1lKHRhcmdldCk7XFxuJyA6ICcnO1xuICBjb25zdCBwYXJlbnRUaGVtZUdsb2JhbEltcG9ydCA9IHRoZW1lUHJvcGVydGllcy5wYXJlbnRcbiAgICA/IGBpbXBvcnQgJy4vdGhlbWUtJHt0aGVtZVByb3BlcnRpZXMucGFyZW50fS5nbG9iYWwuZ2VuZXJhdGVkLmpzJztcXG5gXG4gICAgOiAnJztcblxuICBjb25zdCB0aGVtZUlkZW50aWZpZXIgPSAnX3ZhYWRpbnRoZW1lXycgKyB0aGVtZU5hbWUgKyAnXyc7XG4gIGNvbnN0IGx1bW9Dc3NGbGFnID0gJ192YWFkaW50aGVtZWx1bW9pbXBvcnRzXyc7XG4gIGNvbnN0IGdsb2JhbENzc0ZsYWcgPSB0aGVtZUlkZW50aWZpZXIgKyAnZ2xvYmFsQ3NzJztcbiAgY29uc3QgY29tcG9uZW50Q3NzRmxhZyA9IHRoZW1lSWRlbnRpZmllciArICdjb21wb25lbnRDc3MnO1xuXG4gIGlmICghZXhpc3RzU3luYyhzdHlsZXMpKSB7XG4gICAgaWYgKHByb2R1Y3Rpb25Nb2RlKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoYHN0eWxlcy5jc3MgZmlsZSBpcyBtaXNzaW5nIGFuZCBpcyBuZWVkZWQgZm9yICcke3RoZW1lTmFtZX0nIGluIGZvbGRlciAnJHt0aGVtZUZvbGRlcn0nYCk7XG4gICAgfVxuICAgIHdyaXRlRmlsZVN5bmMoXG4gICAgICBzdHlsZXMsXG4gICAgICAnLyogSW1wb3J0IHlvdXIgYXBwbGljYXRpb24gZ2xvYmFsIGNzcyBmaWxlcyBoZXJlIG9yIGFkZCB0aGUgc3R5bGVzIGRpcmVjdGx5IHRvIHRoaXMgZmlsZSAqLycsXG4gICAgICAndXRmOCdcbiAgICApO1xuICB9XG5cbiAgLy8gc3R5bGVzLmNzcyB3aWxsIGFsd2F5cyBiZSBhdmFpbGFibGUgYXMgd2Ugd3JpdGUgb25lIGlmIGl0IGRvZXNuJ3QgZXhpc3QuXG4gIGxldCBmaWxlbmFtZSA9IGJhc2VuYW1lKHN0eWxlcyk7XG4gIGxldCB2YXJpYWJsZSA9IGNhbWVsQ2FzZShmaWxlbmFtZSk7XG5cbiAgLyogTFVNTyAqL1xuICBjb25zdCBsdW1vSW1wb3J0cyA9IHRoZW1lUHJvcGVydGllcy5sdW1vSW1wb3J0cyB8fCBbJ2NvbG9yJywgJ3R5cG9ncmFwaHknXTtcbiAgaWYgKGx1bW9JbXBvcnRzKSB7XG4gICAgbHVtb0ltcG9ydHMuZm9yRWFjaCgobHVtb0ltcG9ydCkgPT4ge1xuICAgICAgaW1wb3J0cy5wdXNoKGBpbXBvcnQgeyAke2x1bW9JbXBvcnR9IH0gZnJvbSAnQHZhYWRpbi92YWFkaW4tbHVtby1zdHlsZXMvJHtsdW1vSW1wb3J0fS5qcyc7XFxuYCk7XG4gICAgICBpZiAobHVtb0ltcG9ydCA9PT0gJ3V0aWxpdHknIHx8IGx1bW9JbXBvcnQgPT09ICdiYWRnZScgfHwgbHVtb0ltcG9ydCA9PT0gJ3R5cG9ncmFwaHknIHx8IGx1bW9JbXBvcnQgPT09ICdjb2xvcicpIHtcbiAgICAgICAgLy8gSW5qZWN0IGludG8gbWFpbiBkb2N1bWVudCB0aGUgc2FtZSB3YXkgYXMgb3RoZXIgTHVtbyBzdHlsZXMgYXJlIGluamVjdGVkXG4gICAgICAgIGltcG9ydHMucHVzaChgaW1wb3J0ICdAdmFhZGluL3ZhYWRpbi1sdW1vLXN0eWxlcy8ke2x1bW9JbXBvcnR9LWdsb2JhbC5qcyc7XFxuYCk7XG4gICAgICB9XG4gICAgfSk7XG5cbiAgICBsdW1vSW1wb3J0cy5mb3JFYWNoKChsdW1vSW1wb3J0KSA9PiB7XG4gICAgICAvLyBMdW1vIGlzIGluamVjdGVkIHRvIHRoZSBkb2N1bWVudCBieSBMdW1vIGl0c2VsZlxuICAgICAgc2hhZG93T25seUNzcy5wdXNoKGByZW1vdmVycy5wdXNoKGluamVjdEdsb2JhbENzcygke2x1bW9JbXBvcnR9LmNzc1RleHQsICcnLCB0YXJnZXQsIHRydWUpKTtcXG5gKTtcbiAgICB9KTtcbiAgfVxuXG4gIC8qIFRoZW1lICovXG4gIGlmICh1c2VEZXZTZXJ2ZXJPckluUHJvZHVjdGlvbk1vZGUpIHtcbiAgICBnbG9iYWxGaWxlQ29udGVudC5wdXNoKHBhcmVudFRoZW1lR2xvYmFsSW1wb3J0KTtcbiAgICBnbG9iYWxGaWxlQ29udGVudC5wdXNoKGBpbXBvcnQgJ3RoZW1lcy8ke3RoZW1lTmFtZX0vJHtmaWxlbmFtZX0nO1xcbmApO1xuXG4gICAgaW1wb3J0cy5wdXNoKGBpbXBvcnQgJHt2YXJpYWJsZX0gZnJvbSAndGhlbWVzLyR7dGhlbWVOYW1lfS8ke2ZpbGVuYW1lfT9pbmxpbmUnO1xcbmApO1xuICAgIHNoYWRvd09ubHlDc3MucHVzaChgcmVtb3ZlcnMucHVzaChpbmplY3RHbG9iYWxDc3MoJHt2YXJpYWJsZX0udG9TdHJpbmcoKSwgJycsIHRhcmdldCkpO1xcbiAgICBgKTtcbiAgfVxuICBpZiAoZXhpc3RzU3luYyhkb2N1bWVudENzc0ZpbGUpKSB7XG4gICAgZmlsZW5hbWUgPSBiYXNlbmFtZShkb2N1bWVudENzc0ZpbGUpO1xuICAgIHZhcmlhYmxlID0gY2FtZWxDYXNlKGZpbGVuYW1lKTtcblxuICAgIGlmICh1c2VEZXZTZXJ2ZXJPckluUHJvZHVjdGlvbk1vZGUpIHtcbiAgICAgIGdsb2JhbEZpbGVDb250ZW50LnB1c2goYGltcG9ydCAndGhlbWVzLyR7dGhlbWVOYW1lfS8ke2ZpbGVuYW1lfSc7XFxuYCk7XG5cbiAgICAgIGltcG9ydHMucHVzaChgaW1wb3J0ICR7dmFyaWFibGV9IGZyb20gJ3RoZW1lcy8ke3RoZW1lTmFtZX0vJHtmaWxlbmFtZX0/aW5saW5lJztcXG5gKTtcbiAgICAgIHNoYWRvd09ubHlDc3MucHVzaChgcmVtb3ZlcnMucHVzaChpbmplY3RHbG9iYWxDc3MoJHt2YXJpYWJsZX0udG9TdHJpbmcoKSwnJywgZG9jdW1lbnQpKTtcXG4gICAgYCk7XG4gICAgfVxuICB9XG5cbiAgbGV0IGkgPSAwO1xuICBpZiAodGhlbWVQcm9wZXJ0aWVzLmRvY3VtZW50Q3NzKSB7XG4gICAgY29uc3QgbWlzc2luZ01vZHVsZXMgPSBjaGVja01vZHVsZXModGhlbWVQcm9wZXJ0aWVzLmRvY3VtZW50Q3NzKTtcbiAgICBpZiAobWlzc2luZ01vZHVsZXMubGVuZ3RoID4gMCkge1xuICAgICAgdGhyb3cgRXJyb3IoXG4gICAgICAgIFwiTWlzc2luZyBucG0gbW9kdWxlcyBvciBmaWxlcyAnXCIgK1xuICAgICAgICAgIG1pc3NpbmdNb2R1bGVzLmpvaW4oXCInLCAnXCIpICtcbiAgICAgICAgICBcIicgZm9yIGRvY3VtZW50Q3NzIG1hcmtlZCBpbiAndGhlbWUuanNvbicuXFxuXCIgK1xuICAgICAgICAgIFwiSW5zdGFsbCBvciB1cGRhdGUgcGFja2FnZShzKSBieSBhZGRpbmcgYSBATnBtUGFja2FnZSBhbm5vdGF0aW9uIG9yIGluc3RhbGwgaXQgdXNpbmcgJ25wbS9wbnBtIGknXCJcbiAgICAgICk7XG4gICAgfVxuICAgIHRoZW1lUHJvcGVydGllcy5kb2N1bWVudENzcy5mb3JFYWNoKChjc3NJbXBvcnQpID0+IHtcbiAgICAgIGNvbnN0IHZhcmlhYmxlID0gJ21vZHVsZScgKyBpKys7XG4gICAgICBpbXBvcnRzLnB1c2goYGltcG9ydCAke3ZhcmlhYmxlfSBmcm9tICcke2Nzc0ltcG9ydH0/aW5saW5lJztcXG5gKTtcbiAgICAgIC8vIER1ZSB0byBjaHJvbWUgYnVnIGh0dHBzOi8vYnVncy5jaHJvbWl1bS5vcmcvcC9jaHJvbWl1bS9pc3N1ZXMvZGV0YWlsP2lkPTMzNjg3NiBmb250LWZhY2Ugd2lsbCBub3Qgd29ya1xuICAgICAgLy8gaW5zaWRlIHNoYWRvd1Jvb3Qgc28gd2UgbmVlZCB0byBpbmplY3QgaXQgdGhlcmUgYWxzby5cbiAgICAgIGdsb2JhbENzc0NvZGUucHVzaChgaWYodGFyZ2V0ICE9PSBkb2N1bWVudCkge1xuICAgICAgICByZW1vdmVycy5wdXNoKGluamVjdEdsb2JhbENzcygke3ZhcmlhYmxlfS50b1N0cmluZygpLCAnJywgdGFyZ2V0KSk7XG4gICAgfVxcbiAgICBgKTtcbiAgICAgIGdsb2JhbENzc0NvZGUucHVzaChcbiAgICAgICAgYHJlbW92ZXJzLnB1c2goaW5qZWN0R2xvYmFsQ3NzKCR7dmFyaWFibGV9LnRvU3RyaW5nKCksICcke0NTU0lNUE9SVF9DT01NRU5UfScsIGRvY3VtZW50KSk7XFxuICAgIGBcbiAgICAgICk7XG4gICAgfSk7XG4gIH1cbiAgaWYgKHRoZW1lUHJvcGVydGllcy5pbXBvcnRDc3MpIHtcbiAgICBjb25zdCBtaXNzaW5nTW9kdWxlcyA9IGNoZWNrTW9kdWxlcyh0aGVtZVByb3BlcnRpZXMuaW1wb3J0Q3NzKTtcbiAgICBpZiAobWlzc2luZ01vZHVsZXMubGVuZ3RoID4gMCkge1xuICAgICAgdGhyb3cgRXJyb3IoXG4gICAgICAgIFwiTWlzc2luZyBucG0gbW9kdWxlcyBvciBmaWxlcyAnXCIgK1xuICAgICAgICAgIG1pc3NpbmdNb2R1bGVzLmpvaW4oXCInLCAnXCIpICtcbiAgICAgICAgICBcIicgZm9yIGltcG9ydENzcyBtYXJrZWQgaW4gJ3RoZW1lLmpzb24nLlxcblwiICtcbiAgICAgICAgICBcIkluc3RhbGwgb3IgdXBkYXRlIHBhY2thZ2UocykgYnkgYWRkaW5nIGEgQE5wbVBhY2thZ2UgYW5ub3RhdGlvbiBvciBpbnN0YWxsIGl0IHVzaW5nICducG0vcG5wbSBpJ1wiXG4gICAgICApO1xuICAgIH1cbiAgICB0aGVtZVByb3BlcnRpZXMuaW1wb3J0Q3NzLmZvckVhY2goKGNzc1BhdGgpID0+IHtcbiAgICAgIGNvbnN0IHZhcmlhYmxlID0gJ21vZHVsZScgKyBpKys7XG4gICAgICBnbG9iYWxGaWxlQ29udGVudC5wdXNoKGBpbXBvcnQgJyR7Y3NzUGF0aH0nO1xcbmApO1xuICAgICAgaW1wb3J0cy5wdXNoKGBpbXBvcnQgJHt2YXJpYWJsZX0gZnJvbSAnJHtjc3NQYXRofT9pbmxpbmUnO1xcbmApO1xuICAgICAgc2hhZG93T25seUNzcy5wdXNoKGByZW1vdmVycy5wdXNoKGluamVjdEdsb2JhbENzcygke3ZhcmlhYmxlfS50b1N0cmluZygpLCAnJHtDU1NJTVBPUlRfQ09NTUVOVH0nLCB0YXJnZXQpKTtcXG5gKTtcbiAgICB9KTtcbiAgfVxuXG4gIGlmIChhdXRvSW5qZWN0Q29tcG9uZW50cykge1xuICAgIGNvbXBvbmVudHNGaWxlcy5mb3JFYWNoKChjb21wb25lbnRDc3MpID0+IHtcbiAgICAgIGNvbnN0IGZpbGVuYW1lID0gYmFzZW5hbWUoY29tcG9uZW50Q3NzKTtcbiAgICAgIGNvbnN0IHRhZyA9IGZpbGVuYW1lLnJlcGxhY2UoJy5jc3MnLCAnJyk7XG4gICAgICBjb25zdCB2YXJpYWJsZSA9IGNhbWVsQ2FzZShmaWxlbmFtZSk7XG4gICAgICBjb21wb25lbnRDc3NJbXBvcnRzLnB1c2goXG4gICAgICAgIGBpbXBvcnQgJHt2YXJpYWJsZX0gZnJvbSAndGhlbWVzLyR7dGhlbWVOYW1lfS8ke3RoZW1lQ29tcG9uZW50c0ZvbGRlcn0vJHtmaWxlbmFtZX0/aW5saW5lJztcXG5gXG4gICAgICApO1xuICAgICAgLy8gRG9uJ3QgZm9ybWF0IGFzIHRoZSBnZW5lcmF0ZWQgZmlsZSBmb3JtYXR0aW5nIHdpbGwgZ2V0IHdvbmt5IVxuICAgICAgY29uc3QgY29tcG9uZW50U3RyaW5nID0gYHJlZ2lzdGVyU3R5bGVzKFxuICAgICAgICAnJHt0YWd9JyxcbiAgICAgICAgdW5zYWZlQ1NTKCR7dmFyaWFibGV9LnRvU3RyaW5nKCkpXG4gICAgICApO1xuICAgICAgYDtcbiAgICAgIGNvbXBvbmVudENzc0NvZGUucHVzaChjb21wb25lbnRTdHJpbmcpO1xuICAgIH0pO1xuICB9XG5cbiAgdGhlbWVGaWxlQ29udGVudCArPSBpbXBvcnRzLmpvaW4oJycpO1xuXG4gIC8vIERvbid0IGZvcm1hdCBhcyB0aGUgZ2VuZXJhdGVkIGZpbGUgZm9ybWF0dGluZyB3aWxsIGdldCB3b25reSFcbiAgLy8gSWYgdGFyZ2V0cyBjaGVjayB0aGF0IHdlIG9ubHkgcmVnaXN0ZXIgdGhlIHN0eWxlIHBhcnRzIG9uY2UsIGNoZWNrcyBleGlzdCBmb3IgZ2xvYmFsIGNzcyBhbmQgY29tcG9uZW50IGNzc1xuICBjb25zdCB0aGVtZUZpbGVBcHBseSA9IGBcbiAgbGV0IHRoZW1lUmVtb3ZlcnMgPSBuZXcgV2Vha01hcCgpO1xuICBsZXQgdGFyZ2V0cyA9IFtdO1xuXG4gIGV4cG9ydCBjb25zdCBhcHBseVRoZW1lID0gKHRhcmdldCkgPT4ge1xuICAgIGNvbnN0IHJlbW92ZXJzID0gW107XG4gICAgaWYgKHRhcmdldCAhPT0gZG9jdW1lbnQpIHtcbiAgICAgICR7c2hhZG93T25seUNzcy5qb2luKCcnKX1cbiAgICB9XG4gICAgJHtwYXJlbnRUaGVtZX1cbiAgICAke2dsb2JhbENzc0NvZGUuam9pbignJyl9XG5cbiAgICBpZiAoaW1wb3J0Lm1ldGEuaG90KSB7XG4gICAgICB0YXJnZXRzLnB1c2gobmV3IFdlYWtSZWYodGFyZ2V0KSk7XG4gICAgICB0aGVtZVJlbW92ZXJzLnNldCh0YXJnZXQsIHJlbW92ZXJzKTtcbiAgICB9XG5cbiAgfVxuICBcbmA7XG4gIGNvbXBvbmVudHNGaWxlQ29udGVudCArPSBgXG4ke2NvbXBvbmVudENzc0ltcG9ydHMuam9pbignJyl9XG5cbmlmICghZG9jdW1lbnRbJyR7Y29tcG9uZW50Q3NzRmxhZ30nXSkge1xuICAke2NvbXBvbmVudENzc0NvZGUuam9pbignJyl9XG4gIGRvY3VtZW50Wycke2NvbXBvbmVudENzc0ZsYWd9J10gPSB0cnVlO1xufVxuXG5pZiAoaW1wb3J0Lm1ldGEuaG90KSB7XG4gIGltcG9ydC5tZXRhLmhvdC5hY2NlcHQoKG1vZHVsZSkgPT4ge1xuICAgIHdpbmRvdy5sb2NhdGlvbi5yZWxvYWQoKTtcbiAgfSk7XG59XG5cbmA7XG5cbiAgdGhlbWVGaWxlQ29udGVudCArPSB0aGVtZUZpbGVBcHBseTtcbiAgdGhlbWVGaWxlQ29udGVudCArPSBgXG5pZiAoaW1wb3J0Lm1ldGEuaG90KSB7XG4gIGltcG9ydC5tZXRhLmhvdC5hY2NlcHQoKG1vZHVsZSkgPT4ge1xuXG4gICAgaWYgKG5lZWRzUmVsb2FkT25DaGFuZ2VzKSB7XG4gICAgICB3aW5kb3cubG9jYXRpb24ucmVsb2FkKCk7XG4gICAgfSBlbHNlIHtcbiAgICAgIHRhcmdldHMuZm9yRWFjaCh0YXJnZXRSZWYgPT4ge1xuICAgICAgICBjb25zdCB0YXJnZXQgPSB0YXJnZXRSZWYuZGVyZWYoKTtcbiAgICAgICAgaWYgKHRhcmdldCkge1xuICAgICAgICAgIHRoZW1lUmVtb3ZlcnMuZ2V0KHRhcmdldCkuZm9yRWFjaChyZW1vdmVyID0+IHJlbW92ZXIoKSlcbiAgICAgICAgICBtb2R1bGUuYXBwbHlUaGVtZSh0YXJnZXQpO1xuICAgICAgICB9XG4gICAgICB9KVxuICAgIH1cbiAgfSk7XG5cbiAgaW1wb3J0Lm1ldGEuaG90Lm9uKCd2aXRlOmFmdGVyVXBkYXRlJywgKHVwZGF0ZSkgPT4ge1xuICAgIGRvY3VtZW50LmRpc3BhdGNoRXZlbnQobmV3IEN1c3RvbUV2ZW50KCd2YWFkaW4tdGhlbWUtdXBkYXRlZCcsIHsgZGV0YWlsOiB1cGRhdGUgfSkpO1xuICB9KTtcbn1cblxuYDtcblxuICBnbG9iYWxJbXBvcnRDb250ZW50ICs9IGBcbiR7Z2xvYmFsRmlsZUNvbnRlbnQuam9pbignJyl9XG5gO1xuXG4gIHdyaXRlSWZDaGFuZ2VkKHJlc29sdmUob3V0cHV0Rm9sZGVyLCBnbG9iYWxGaWxlbmFtZSksIGdsb2JhbEltcG9ydENvbnRlbnQpO1xuICB3cml0ZUlmQ2hhbmdlZChyZXNvbHZlKG91dHB1dEZvbGRlciwgdGhlbWVGaWxlbmFtZSksIHRoZW1lRmlsZUNvbnRlbnQpO1xuICB3cml0ZUlmQ2hhbmdlZChyZXNvbHZlKG91dHB1dEZvbGRlciwgY29tcG9uZW50c0ZpbGVuYW1lKSwgY29tcG9uZW50c0ZpbGVDb250ZW50KTtcbn1cblxuZnVuY3Rpb24gd3JpdGVJZkNoYW5nZWQoZmlsZSwgZGF0YSkge1xuICBpZiAoIWV4aXN0c1N5bmMoZmlsZSkgfHwgcmVhZEZpbGVTeW5jKGZpbGUsIHsgZW5jb2Rpbmc6ICd1dGYtOCcgfSkgIT09IGRhdGEpIHtcbiAgICB3cml0ZUZpbGVTeW5jKGZpbGUsIGRhdGEpO1xuICB9XG59XG5cbi8qKlxuICogTWFrZSBnaXZlbiBzdHJpbmcgaW50byBjYW1lbENhc2UuXG4gKlxuICogQHBhcmFtIHtzdHJpbmd9IHN0ciBzdHJpbmcgdG8gbWFrZSBpbnRvIGNhbWVDYXNlXG4gKiBAcmV0dXJucyB7c3RyaW5nfSBjYW1lbENhc2VkIHZlcnNpb25cbiAqL1xuZnVuY3Rpb24gY2FtZWxDYXNlKHN0cikge1xuICByZXR1cm4gc3RyXG4gICAgLnJlcGxhY2UoLyg/Ol5cXHd8W0EtWl18XFxiXFx3KS9nLCBmdW5jdGlvbiAod29yZCwgaW5kZXgpIHtcbiAgICAgIHJldHVybiBpbmRleCA9PT0gMCA/IHdvcmQudG9Mb3dlckNhc2UoKSA6IHdvcmQudG9VcHBlckNhc2UoKTtcbiAgICB9KVxuICAgIC5yZXBsYWNlKC9cXHMrL2csICcnKVxuICAgIC5yZXBsYWNlKC9cXC58XFwtL2csICcnKTtcbn1cblxuZXhwb3J0IHsgd3JpdGVUaGVtZUZpbGVzIH07XG4iLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkY6XFxcXEVjbGlwc2UgV29ya3NwYWNlXFxcXG1lZ3BiclxcXFx0YXJnZXRcXFxccGx1Z2luc1xcXFxhcHBsaWNhdGlvbi10aGVtZS1wbHVnaW5cIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkY6XFxcXEVjbGlwc2UgV29ya3NwYWNlXFxcXG1lZ3BiclxcXFx0YXJnZXRcXFxccGx1Z2luc1xcXFxhcHBsaWNhdGlvbi10aGVtZS1wbHVnaW5cXFxcdGhlbWUtY29weS5qc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vRjovRWNsaXBzZSUyMFdvcmtzcGFjZS9tZWdwYnIvdGFyZ2V0L3BsdWdpbnMvYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luL3RoZW1lLWNvcHkuanNcIjsvKlxuICogQ29weXJpZ2h0IDIwMDAtMjAyMyBWYWFkaW4gTHRkLlxuICpcbiAqIExpY2Vuc2VkIHVuZGVyIHRoZSBBcGFjaGUgTGljZW5zZSwgVmVyc2lvbiAyLjAgKHRoZSBcIkxpY2Vuc2VcIik7IHlvdSBtYXkgbm90XG4gKiB1c2UgdGhpcyBmaWxlIGV4Y2VwdCBpbiBjb21wbGlhbmNlIHdpdGggdGhlIExpY2Vuc2UuIFlvdSBtYXkgb2J0YWluIGEgY29weSBvZlxuICogdGhlIExpY2Vuc2UgYXRcbiAqXG4gKiBodHRwOi8vd3d3LmFwYWNoZS5vcmcvbGljZW5zZXMvTElDRU5TRS0yLjBcbiAqXG4gKiBVbmxlc3MgcmVxdWlyZWQgYnkgYXBwbGljYWJsZSBsYXcgb3IgYWdyZWVkIHRvIGluIHdyaXRpbmcsIHNvZnR3YXJlXG4gKiBkaXN0cmlidXRlZCB1bmRlciB0aGUgTGljZW5zZSBpcyBkaXN0cmlidXRlZCBvbiBhbiBcIkFTIElTXCIgQkFTSVMsIFdJVEhPVVRcbiAqIFdBUlJBTlRJRVMgT1IgQ09ORElUSU9OUyBPRiBBTlkgS0lORCwgZWl0aGVyIGV4cHJlc3Mgb3IgaW1wbGllZC4gU2VlIHRoZVxuICogTGljZW5zZSBmb3IgdGhlIHNwZWNpZmljIGxhbmd1YWdlIGdvdmVybmluZyBwZXJtaXNzaW9ucyBhbmQgbGltaXRhdGlvbnMgdW5kZXJcbiAqIHRoZSBMaWNlbnNlLlxuICovXG5cbi8qKlxuICogVGhpcyBjb250YWlucyBmdW5jdGlvbnMgYW5kIGZlYXR1cmVzIHVzZWQgdG8gY29weSB0aGVtZSBmaWxlcy5cbiAqL1xuXG5pbXBvcnQgeyByZWFkZGlyU3luYywgc3RhdFN5bmMsIG1rZGlyU3luYywgZXhpc3RzU3luYywgY29weUZpbGVTeW5jIH0gZnJvbSAnZnMnO1xuaW1wb3J0IHsgcmVzb2x2ZSwgYmFzZW5hbWUsIHJlbGF0aXZlLCBleHRuYW1lIH0gZnJvbSAncGF0aCc7XG5pbXBvcnQgeyBnbG9iU3luYyB9IGZyb20gJ2dsb2InO1xuXG5jb25zdCBpZ25vcmVkRmlsZUV4dGVuc2lvbnMgPSBbJy5jc3MnLCAnLmpzJywgJy5qc29uJ107XG5cbi8qKlxuICogQ29weSB0aGVtZSBzdGF0aWMgcmVzb3VyY2VzIHRvIHN0YXRpYyBhc3NldHMgZm9sZGVyLiBBbGwgZmlsZXMgaW4gdGhlIHRoZW1lXG4gKiBmb2xkZXIgd2lsbCBiZSBjb3BpZWQgZXhjbHVkaW5nIGNzcywganMgYW5kIGpzb24gZmlsZXMgdGhhdCB3aWxsIGJlXG4gKiBoYW5kbGVkIGJ5IHdlYnBhY2sgYW5kIG5vdCBiZSBzaGFyZWQgYXMgc3RhdGljIGZpbGVzLlxuICpcbiAqIEBwYXJhbSB7c3RyaW5nfSB0aGVtZUZvbGRlciBGb2xkZXIgd2l0aCB0aGVtZSBmaWxlXG4gKiBAcGFyYW0ge3N0cmluZ30gcHJvamVjdFN0YXRpY0Fzc2V0c091dHB1dEZvbGRlciByZXNvdXJjZXMgb3V0cHV0IGZvbGRlclxuICogQHBhcmFtIHtvYmplY3R9IGxvZ2dlciBwbHVnaW4gbG9nZ2VyXG4gKi9cbmZ1bmN0aW9uIGNvcHlUaGVtZVJlc291cmNlcyh0aGVtZUZvbGRlciwgcHJvamVjdFN0YXRpY0Fzc2V0c091dHB1dEZvbGRlciwgbG9nZ2VyKSB7XG4gIGNvbnN0IHN0YXRpY0Fzc2V0c1RoZW1lRm9sZGVyID0gcmVzb2x2ZShwcm9qZWN0U3RhdGljQXNzZXRzT3V0cHV0Rm9sZGVyLCAndGhlbWVzJywgYmFzZW5hbWUodGhlbWVGb2xkZXIpKTtcbiAgY29uc3QgY29sbGVjdGlvbiA9IGNvbGxlY3RGb2xkZXJzKHRoZW1lRm9sZGVyLCBsb2dnZXIpO1xuXG4gIC8vIE9ubHkgY3JlYXRlIGFzc2V0cyBmb2xkZXIgaWYgdGhlcmUgYXJlIGZpbGVzIHRvIGNvcHkuXG4gIGlmIChjb2xsZWN0aW9uLmZpbGVzLmxlbmd0aCA+IDApIHtcbiAgICBta2RpclN5bmMoc3RhdGljQXNzZXRzVGhlbWVGb2xkZXIsIHsgcmVjdXJzaXZlOiB0cnVlIH0pO1xuICAgIC8vIGNyZWF0ZSBmb2xkZXJzIHdpdGhcbiAgICBjb2xsZWN0aW9uLmRpcmVjdG9yaWVzLmZvckVhY2goKGRpcmVjdG9yeSkgPT4ge1xuICAgICAgY29uc3QgcmVsYXRpdmVEaXJlY3RvcnkgPSByZWxhdGl2ZSh0aGVtZUZvbGRlciwgZGlyZWN0b3J5KTtcbiAgICAgIGNvbnN0IHRhcmdldERpcmVjdG9yeSA9IHJlc29sdmUoc3RhdGljQXNzZXRzVGhlbWVGb2xkZXIsIHJlbGF0aXZlRGlyZWN0b3J5KTtcblxuICAgICAgbWtkaXJTeW5jKHRhcmdldERpcmVjdG9yeSwgeyByZWN1cnNpdmU6IHRydWUgfSk7XG4gICAgfSk7XG5cbiAgICBjb2xsZWN0aW9uLmZpbGVzLmZvckVhY2goKGZpbGUpID0+IHtcbiAgICAgIGNvbnN0IHJlbGF0aXZlRmlsZSA9IHJlbGF0aXZlKHRoZW1lRm9sZGVyLCBmaWxlKTtcbiAgICAgIGNvbnN0IHRhcmdldEZpbGUgPSByZXNvbHZlKHN0YXRpY0Fzc2V0c1RoZW1lRm9sZGVyLCByZWxhdGl2ZUZpbGUpO1xuICAgICAgY29weUZpbGVJZkFic2VudE9yTmV3ZXIoZmlsZSwgdGFyZ2V0RmlsZSwgbG9nZ2VyKTtcbiAgICB9KTtcbiAgfVxufVxuXG4vKipcbiAqIENvbGxlY3QgYWxsIGZvbGRlcnMgd2l0aCBjb3B5YWJsZSBmaWxlcyBhbmQgYWxsIGZpbGVzIHRvIGJlIGNvcGllZC5cbiAqIEZvbGVkIHdpbGwgbm90IGJlIGFkZGVkIGlmIG5vIGZpbGVzIGluIGZvbGRlciBvciBzdWJmb2xkZXJzLlxuICpcbiAqIEZpbGVzIHdpbGwgbm90IGNvbnRhaW4gZmlsZXMgd2l0aCBpZ25vcmVkIGV4dGVuc2lvbnMgYW5kIGZvbGRlcnMgb25seSBjb250YWluaW5nIGlnbm9yZWQgZmlsZXMgd2lsbCBub3QgYmUgYWRkZWQuXG4gKlxuICogQHBhcmFtIGZvbGRlclRvQ29weSBmb2xkZXIgd2Ugd2lsbCBjb3B5IGZpbGVzIGZyb21cbiAqIEBwYXJhbSBsb2dnZXIgcGx1Z2luIGxvZ2dlclxuICogQHJldHVybiB7e2RpcmVjdG9yaWVzOiBbXSwgZmlsZXM6IFtdfX0gb2JqZWN0IGNvbnRhaW5pbmcgZGlyZWN0b3JpZXMgdG8gY3JlYXRlIGFuZCBmaWxlcyB0byBjb3B5XG4gKi9cbmZ1bmN0aW9uIGNvbGxlY3RGb2xkZXJzKGZvbGRlclRvQ29weSwgbG9nZ2VyKSB7XG4gIGNvbnN0IGNvbGxlY3Rpb24gPSB7IGRpcmVjdG9yaWVzOiBbXSwgZmlsZXM6IFtdIH07XG4gIGxvZ2dlci50cmFjZSgnZmlsZXMgaW4gZGlyZWN0b3J5JywgcmVhZGRpclN5bmMoZm9sZGVyVG9Db3B5KSk7XG4gIHJlYWRkaXJTeW5jKGZvbGRlclRvQ29weSkuZm9yRWFjaCgoZmlsZSkgPT4ge1xuICAgIGNvbnN0IGZpbGVUb0NvcHkgPSByZXNvbHZlKGZvbGRlclRvQ29weSwgZmlsZSk7XG4gICAgdHJ5IHtcbiAgICAgIGlmIChzdGF0U3luYyhmaWxlVG9Db3B5KS5pc0RpcmVjdG9yeSgpKSB7XG4gICAgICAgIGxvZ2dlci5kZWJ1ZygnR29pbmcgdGhyb3VnaCBkaXJlY3RvcnknLCBmaWxlVG9Db3B5KTtcbiAgICAgICAgY29uc3QgcmVzdWx0ID0gY29sbGVjdEZvbGRlcnMoZmlsZVRvQ29weSwgbG9nZ2VyKTtcbiAgICAgICAgaWYgKHJlc3VsdC5maWxlcy5sZW5ndGggPiAwKSB7XG4gICAgICAgICAgY29sbGVjdGlvbi5kaXJlY3Rvcmllcy5wdXNoKGZpbGVUb0NvcHkpO1xuICAgICAgICAgIGxvZ2dlci5kZWJ1ZygnQWRkaW5nIGRpcmVjdG9yeScsIGZpbGVUb0NvcHkpO1xuICAgICAgICAgIGNvbGxlY3Rpb24uZGlyZWN0b3JpZXMucHVzaC5hcHBseShjb2xsZWN0aW9uLmRpcmVjdG9yaWVzLCByZXN1bHQuZGlyZWN0b3JpZXMpO1xuICAgICAgICAgIGNvbGxlY3Rpb24uZmlsZXMucHVzaC5hcHBseShjb2xsZWN0aW9uLmZpbGVzLCByZXN1bHQuZmlsZXMpO1xuICAgICAgICB9XG4gICAgICB9IGVsc2UgaWYgKCFpZ25vcmVkRmlsZUV4dGVuc2lvbnMuaW5jbHVkZXMoZXh0bmFtZShmaWxlVG9Db3B5KSkpIHtcbiAgICAgICAgbG9nZ2VyLmRlYnVnKCdBZGRpbmcgZmlsZScsIGZpbGVUb0NvcHkpO1xuICAgICAgICBjb2xsZWN0aW9uLmZpbGVzLnB1c2goZmlsZVRvQ29weSk7XG4gICAgICB9XG4gICAgfSBjYXRjaCAoZXJyb3IpIHtcbiAgICAgIGhhbmRsZU5vU3VjaEZpbGVFcnJvcihmaWxlVG9Db3B5LCBlcnJvciwgbG9nZ2VyKTtcbiAgICB9XG4gIH0pO1xuICByZXR1cm4gY29sbGVjdGlvbjtcbn1cblxuLyoqXG4gKiBDb3B5IGFueSBzdGF0aWMgbm9kZV9tb2R1bGVzIGFzc2V0cyBtYXJrZWQgaW4gdGhlbWUuanNvbiB0b1xuICogcHJvamVjdCBzdGF0aWMgYXNzZXRzIGZvbGRlci5cbiAqXG4gKiBUaGUgdGhlbWUuanNvbiBjb250ZW50IGZvciBhc3NldHMgaXMgc2V0IHVwIGFzOlxuICoge1xuICogICBhc3NldHM6IHtcbiAqICAgICBcIm5vZGVfbW9kdWxlIGlkZW50aWZpZXJcIjoge1xuICogICAgICAgXCJjb3B5LXJ1bGVcIjogXCJ0YXJnZXQvZm9sZGVyXCIsXG4gKiAgICAgfVxuICogICB9XG4gKiB9XG4gKlxuICogVGhpcyB3b3VsZCBtZWFuIHRoYXQgYW4gYXNzZXQgd291bGQgYmUgYnVpbHQgYXM6XG4gKiBcIkBmb3J0YXdlc29tZS9mb250YXdlc29tZS1mcmVlXCI6IHtcbiAqICAgXCJzdmdzL3JlZ3VsYXIvKipcIjogXCJmb3J0YXdlc29tZS9pY29uc1wiXG4gKiB9XG4gKiBXaGVyZSAnQGZvcnRhd2Vzb21lL2ZvbnRhd2Vzb21lLWZyZWUnIGlzIHRoZSBucG0gcGFja2FnZSwgJ3N2Z3MvcmVndWxhci8qKicgaXMgd2hhdCBzaG91bGQgYmUgY29waWVkXG4gKiBhbmQgJ2ZvcnRhd2Vzb21lL2ljb25zJyBpcyB0aGUgdGFyZ2V0IGRpcmVjdG9yeSB1bmRlciBwcm9qZWN0U3RhdGljQXNzZXRzT3V0cHV0Rm9sZGVyIHdoZXJlIHRoaW5nc1xuICogd2lsbCBnZXQgY29waWVkIHRvLlxuICpcbiAqIE5vdGUhIHRoZXJlIGNhbiBiZSBtdWx0aXBsZSBjb3B5LXJ1bGVzIHdpdGggdGFyZ2V0IGZvbGRlcnMgZm9yIG9uZSBucG0gcGFja2FnZSBhc3NldC5cbiAqXG4gKiBAcGFyYW0ge3N0cmluZ30gdGhlbWVOYW1lIG5hbWUgb2YgdGhlIHRoZW1lIHdlIGFyZSBjb3B5aW5nIGFzc2V0cyBmb3JcbiAqIEBwYXJhbSB7anNvbn0gdGhlbWVQcm9wZXJ0aWVzIHRoZW1lIHByb3BlcnRpZXMganNvbiB3aXRoIGRhdGEgb24gYXNzZXRzXG4gKiBAcGFyYW0ge3N0cmluZ30gcHJvamVjdFN0YXRpY0Fzc2V0c091dHB1dEZvbGRlciBwcm9qZWN0IG91dHB1dCBmb2xkZXIgd2hlcmUgd2UgY29weSBhc3NldHMgdG8gdW5kZXIgdGhlbWUvW3RoZW1lTmFtZV1cbiAqIEBwYXJhbSB7b2JqZWN0fSBsb2dnZXIgcGx1Z2luIGxvZ2dlclxuICovXG5mdW5jdGlvbiBjb3B5U3RhdGljQXNzZXRzKHRoZW1lTmFtZSwgdGhlbWVQcm9wZXJ0aWVzLCBwcm9qZWN0U3RhdGljQXNzZXRzT3V0cHV0Rm9sZGVyLCBsb2dnZXIpIHtcbiAgY29uc3QgYXNzZXRzID0gdGhlbWVQcm9wZXJ0aWVzWydhc3NldHMnXTtcbiAgaWYgKCFhc3NldHMpIHtcbiAgICBsb2dnZXIuZGVidWcoJ25vIGFzc2V0cyB0byBoYW5kbGUgbm8gc3RhdGljIGFzc2V0cyB3ZXJlIGNvcGllZCcpO1xuICAgIHJldHVybjtcbiAgfVxuXG4gIG1rZGlyU3luYyhwcm9qZWN0U3RhdGljQXNzZXRzT3V0cHV0Rm9sZGVyLCB7XG4gICAgcmVjdXJzaXZlOiB0cnVlXG4gIH0pO1xuICBjb25zdCBtaXNzaW5nTW9kdWxlcyA9IGNoZWNrTW9kdWxlcyhPYmplY3Qua2V5cyhhc3NldHMpKTtcbiAgaWYgKG1pc3NpbmdNb2R1bGVzLmxlbmd0aCA+IDApIHtcbiAgICB0aHJvdyBFcnJvcihcbiAgICAgIFwiTWlzc2luZyBucG0gbW9kdWxlcyAnXCIgK1xuICAgICAgICBtaXNzaW5nTW9kdWxlcy5qb2luKFwiJywgJ1wiKSArXG4gICAgICAgIFwiJyBmb3IgYXNzZXRzIG1hcmtlZCBpbiAndGhlbWUuanNvbicuXFxuXCIgK1xuICAgICAgICBcIkluc3RhbGwgcGFja2FnZShzKSBieSBhZGRpbmcgYSBATnBtUGFja2FnZSBhbm5vdGF0aW9uIG9yIGluc3RhbGwgaXQgdXNpbmcgJ25wbS9wbnBtIGknXCJcbiAgICApO1xuICB9XG4gIE9iamVjdC5rZXlzKGFzc2V0cykuZm9yRWFjaCgobW9kdWxlKSA9PiB7XG4gICAgY29uc3QgY29weVJ1bGVzID0gYXNzZXRzW21vZHVsZV07XG4gICAgT2JqZWN0LmtleXMoY29weVJ1bGVzKS5mb3JFYWNoKChjb3B5UnVsZSkgPT4ge1xuICAgICAgY29uc3Qgbm9kZVNvdXJjZXMgPSByZXNvbHZlKCdub2RlX21vZHVsZXMvJywgbW9kdWxlLCBjb3B5UnVsZSk7XG4gICAgICBjb25zdCBmaWxlcyA9IGdsb2JTeW5jKG5vZGVTb3VyY2VzLCB7IG5vZGlyOiB0cnVlIH0pO1xuICAgICAgY29uc3QgdGFyZ2V0Rm9sZGVyID0gcmVzb2x2ZShwcm9qZWN0U3RhdGljQXNzZXRzT3V0cHV0Rm9sZGVyLCAndGhlbWVzJywgdGhlbWVOYW1lLCBjb3B5UnVsZXNbY29weVJ1bGVdKTtcblxuICAgICAgbWtkaXJTeW5jKHRhcmdldEZvbGRlciwge1xuICAgICAgICByZWN1cnNpdmU6IHRydWVcbiAgICAgIH0pO1xuICAgICAgZmlsZXMuZm9yRWFjaCgoZmlsZSkgPT4ge1xuICAgICAgICBjb25zdCBjb3B5VGFyZ2V0ID0gcmVzb2x2ZSh0YXJnZXRGb2xkZXIsIGJhc2VuYW1lKGZpbGUpKTtcbiAgICAgICAgY29weUZpbGVJZkFic2VudE9yTmV3ZXIoZmlsZSwgY29weVRhcmdldCwgbG9nZ2VyKTtcbiAgICAgIH0pO1xuICAgIH0pO1xuICB9KTtcbn1cblxuZnVuY3Rpb24gY2hlY2tNb2R1bGVzKG1vZHVsZXMpIHtcbiAgY29uc3QgbWlzc2luZyA9IFtdO1xuXG4gIG1vZHVsZXMuZm9yRWFjaCgobW9kdWxlKSA9PiB7XG4gICAgaWYgKCFleGlzdHNTeW5jKHJlc29sdmUoJ25vZGVfbW9kdWxlcy8nLCBtb2R1bGUpKSkge1xuICAgICAgbWlzc2luZy5wdXNoKG1vZHVsZSk7XG4gICAgfVxuICB9KTtcblxuICByZXR1cm4gbWlzc2luZztcbn1cblxuLyoqXG4gKiBDb3BpZXMgZ2l2ZW4gZmlsZSB0byBhIGdpdmVuIHRhcmdldCBwYXRoLCBpZiB0YXJnZXQgZmlsZSBkb2Vzbid0IGV4aXN0IG9yIGlmXG4gKiBmaWxlIHRvIGNvcHkgaXMgbmV3ZXIuXG4gKiBAcGFyYW0ge3N0cmluZ30gZmlsZVRvQ29weSBwYXRoIG9mIHRoZSBmaWxlIHRvIGNvcHlcbiAqIEBwYXJhbSB7c3RyaW5nfSBjb3B5VGFyZ2V0IHBhdGggb2YgdGhlIHRhcmdldCBmaWxlXG4gKiBAcGFyYW0ge29iamVjdH0gbG9nZ2VyIHBsdWdpbiBsb2dnZXJcbiAqL1xuZnVuY3Rpb24gY29weUZpbGVJZkFic2VudE9yTmV3ZXIoZmlsZVRvQ29weSwgY29weVRhcmdldCwgbG9nZ2VyKSB7XG4gIHRyeSB7XG4gICAgaWYgKCFleGlzdHNTeW5jKGNvcHlUYXJnZXQpIHx8IHN0YXRTeW5jKGNvcHlUYXJnZXQpLm10aW1lIDwgc3RhdFN5bmMoZmlsZVRvQ29weSkubXRpbWUpIHtcbiAgICAgIGxvZ2dlci50cmFjZSgnQ29weWluZzogJywgZmlsZVRvQ29weSwgJz0+JywgY29weVRhcmdldCk7XG4gICAgICBjb3B5RmlsZVN5bmMoZmlsZVRvQ29weSwgY29weVRhcmdldCk7XG4gICAgfVxuICB9IGNhdGNoIChlcnJvcikge1xuICAgIGhhbmRsZU5vU3VjaEZpbGVFcnJvcihmaWxlVG9Db3B5LCBlcnJvciwgbG9nZ2VyKTtcbiAgfVxufVxuXG4vLyBJZ25vcmVzIGVycm9ycyBkdWUgdG8gZmlsZSBtaXNzaW5nIGR1cmluZyB0aGVtZSBwcm9jZXNzaW5nXG4vLyBUaGlzIG1heSBoYXBwZW4gZm9yIGV4YW1wbGUgd2hlbiBhbiBJREUgY3JlYXRlcyBhIHRlbXBvcmFyeSBmaWxlXG4vLyBhbmQgdGhlbiBpbW1lZGlhdGVseSBkZWxldGVzIGl0XG5mdW5jdGlvbiBoYW5kbGVOb1N1Y2hGaWxlRXJyb3IoZmlsZSwgZXJyb3IsIGxvZ2dlcikge1xuICBpZiAoZXJyb3IuY29kZSA9PT0gJ0VOT0VOVCcpIHtcbiAgICBsb2dnZXIud2FybignSWdub3Jpbmcgbm90IGV4aXN0aW5nIGZpbGUgJyArIGZpbGUgKyAnLiBGaWxlIG1heSBoYXZlIGJlZW4gZGVsZXRlZCBkdXJpbmcgdGhlbWUgcHJvY2Vzc2luZy4nKTtcbiAgfSBlbHNlIHtcbiAgICB0aHJvdyBlcnJvcjtcbiAgfVxufVxuXG5leHBvcnQgeyBjaGVja01vZHVsZXMsIGNvcHlTdGF0aWNBc3NldHMsIGNvcHlUaGVtZVJlc291cmNlcyB9O1xuIiwgImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJGOlxcXFxFY2xpcHNlIFdvcmtzcGFjZVxcXFxtZWdwYnJcXFxcdGFyZ2V0XFxcXHBsdWdpbnNcXFxcdGhlbWUtbG9hZGVyXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJGOlxcXFxFY2xpcHNlIFdvcmtzcGFjZVxcXFxtZWdwYnJcXFxcdGFyZ2V0XFxcXHBsdWdpbnNcXFxcdGhlbWUtbG9hZGVyXFxcXHRoZW1lLWxvYWRlci11dGlscy5qc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vRjovRWNsaXBzZSUyMFdvcmtzcGFjZS9tZWdwYnIvdGFyZ2V0L3BsdWdpbnMvdGhlbWUtbG9hZGVyL3RoZW1lLWxvYWRlci11dGlscy5qc1wiO2ltcG9ydCB7IGV4aXN0c1N5bmMsIHJlYWRGaWxlU3luYyB9IGZyb20gJ2ZzJztcbmltcG9ydCB7IHJlc29sdmUsIGJhc2VuYW1lIH0gZnJvbSAncGF0aCc7XG5pbXBvcnQgeyBnbG9iU3luYyB9IGZyb20gJ2dsb2InO1xuXG4vLyBDb2xsZWN0IGdyb3VwcyBbdXJsKF0gWyd8XCJdb3B0aW9uYWwgJy4vfC4uLycsIGZpbGUgcGFydCBhbmQgZW5kIG9mIHVybFxuY29uc3QgdXJsTWF0Y2hlciA9IC8odXJsXFwoXFxzKikoXFwnfFxcXCIpPyhcXC5cXC98XFwuXFwuXFwvKShcXFMqKShcXDJcXHMqXFwpKS9nO1xuXG5mdW5jdGlvbiBhc3NldHNDb250YWlucyhmaWxlVXJsLCB0aGVtZUZvbGRlciwgbG9nZ2VyKSB7XG4gIGNvbnN0IHRoZW1lUHJvcGVydGllcyA9IGdldFRoZW1lUHJvcGVydGllcyh0aGVtZUZvbGRlcik7XG4gIGlmICghdGhlbWVQcm9wZXJ0aWVzKSB7XG4gICAgbG9nZ2VyLmRlYnVnKCdObyB0aGVtZSBwcm9wZXJ0aWVzIGZvdW5kLicpO1xuICAgIHJldHVybiBmYWxzZTtcbiAgfVxuICBjb25zdCBhc3NldHMgPSB0aGVtZVByb3BlcnRpZXNbJ2Fzc2V0cyddO1xuICBpZiAoIWFzc2V0cykge1xuICAgIGxvZ2dlci5kZWJ1ZygnTm8gZGVmaW5lZCBhc3NldHMgaW4gdGhlbWUgcHJvcGVydGllcycpO1xuICAgIHJldHVybiBmYWxzZTtcbiAgfVxuICAvLyBHbyB0aHJvdWdoIGVhY2ggYXNzZXQgbW9kdWxlXG4gIGZvciAobGV0IG1vZHVsZSBvZiBPYmplY3Qua2V5cyhhc3NldHMpKSB7XG4gICAgY29uc3QgY29weVJ1bGVzID0gYXNzZXRzW21vZHVsZV07XG4gICAgLy8gR28gdGhyb3VnaCBlYWNoIGNvcHkgcnVsZVxuICAgIGZvciAobGV0IGNvcHlSdWxlIG9mIE9iamVjdC5rZXlzKGNvcHlSdWxlcykpIHtcbiAgICAgIC8vIGlmIGZpbGUgc3RhcnRzIHdpdGggY29weVJ1bGUgdGFyZ2V0IGNoZWNrIGlmIGZpbGUgd2l0aCBwYXRoIGFmdGVyIGNvcHkgdGFyZ2V0IGNhbiBiZSBmb3VuZFxuICAgICAgaWYgKGZpbGVVcmwuc3RhcnRzV2l0aChjb3B5UnVsZXNbY29weVJ1bGVdKSkge1xuICAgICAgICBjb25zdCB0YXJnZXRGaWxlID0gZmlsZVVybC5yZXBsYWNlKGNvcHlSdWxlc1tjb3B5UnVsZV0sICcnKTtcbiAgICAgICAgY29uc3QgZmlsZXMgPSBnbG9iU3luYyhyZXNvbHZlKCdub2RlX21vZHVsZXMvJywgbW9kdWxlLCBjb3B5UnVsZSksIHsgbm9kaXI6IHRydWUgfSk7XG5cbiAgICAgICAgZm9yIChsZXQgZmlsZSBvZiBmaWxlcykge1xuICAgICAgICAgIGlmIChmaWxlLmVuZHNXaXRoKHRhcmdldEZpbGUpKSByZXR1cm4gdHJ1ZTtcbiAgICAgICAgfVxuICAgICAgfVxuICAgIH1cbiAgfVxuICByZXR1cm4gZmFsc2U7XG59XG5cbmZ1bmN0aW9uIGdldFRoZW1lUHJvcGVydGllcyh0aGVtZUZvbGRlcikge1xuICBjb25zdCB0aGVtZVByb3BlcnR5RmlsZSA9IHJlc29sdmUodGhlbWVGb2xkZXIsICd0aGVtZS5qc29uJyk7XG4gIGlmICghZXhpc3RzU3luYyh0aGVtZVByb3BlcnR5RmlsZSkpIHtcbiAgICByZXR1cm4ge307XG4gIH1cbiAgY29uc3QgdGhlbWVQcm9wZXJ0eUZpbGVBc1N0cmluZyA9IHJlYWRGaWxlU3luYyh0aGVtZVByb3BlcnR5RmlsZSk7XG4gIGlmICh0aGVtZVByb3BlcnR5RmlsZUFzU3RyaW5nLmxlbmd0aCA9PT0gMCkge1xuICAgIHJldHVybiB7fTtcbiAgfVxuICByZXR1cm4gSlNPTi5wYXJzZSh0aGVtZVByb3BlcnR5RmlsZUFzU3RyaW5nKTtcbn1cblxuZnVuY3Rpb24gcmV3cml0ZUNzc1VybHMoc291cmNlLCBoYW5kbGVkUmVzb3VyY2VGb2xkZXIsIHRoZW1lRm9sZGVyLCBsb2dnZXIsIG9wdGlvbnMpIHtcbiAgc291cmNlID0gc291cmNlLnJlcGxhY2UodXJsTWF0Y2hlciwgZnVuY3Rpb24gKG1hdGNoLCB1cmwsIHF1b3RlTWFyaywgcmVwbGFjZSwgZmlsZVVybCwgZW5kU3RyaW5nKSB7XG4gICAgbGV0IGFic29sdXRlUGF0aCA9IHJlc29sdmUoaGFuZGxlZFJlc291cmNlRm9sZGVyLCByZXBsYWNlLCBmaWxlVXJsKTtcbiAgICBjb25zdCBleGlzdGluZ1RoZW1lUmVzb3VyY2UgPSBhYnNvbHV0ZVBhdGguc3RhcnRzV2l0aCh0aGVtZUZvbGRlcikgJiYgZXhpc3RzU3luYyhhYnNvbHV0ZVBhdGgpO1xuICAgIGlmIChleGlzdGluZ1RoZW1lUmVzb3VyY2UgfHwgYXNzZXRzQ29udGFpbnMoZmlsZVVybCwgdGhlbWVGb2xkZXIsIGxvZ2dlcikpIHtcbiAgICAgIC8vIEFkZGluZyAuLyB3aWxsIHNraXAgY3NzLWxvYWRlciwgd2hpY2ggc2hvdWxkIGJlIGRvbmUgZm9yIGFzc2V0IGZpbGVzXG4gICAgICAvLyBJbiBhIHByb2R1Y3Rpb24gYnVpbGQsIHRoZSBjc3MgZmlsZSBpcyBpbiBWQUFESU4vYnVpbGQgYW5kIHN0YXRpYyBmaWxlcyBhcmUgaW4gVkFBRElOL3N0YXRpYywgc28gLi4vc3RhdGljIG5lZWRzIHRvIGJlIGFkZGVkXG4gICAgICBjb25zdCByZXBsYWNlbWVudCA9IG9wdGlvbnMuZGV2TW9kZSA/ICcuLycgOiAnLi4vc3RhdGljLyc7XG5cbiAgICAgIGNvbnN0IHNraXBMb2FkZXIgPSBleGlzdGluZ1RoZW1lUmVzb3VyY2UgPyAnJyA6IHJlcGxhY2VtZW50O1xuICAgICAgY29uc3QgZnJvbnRlbmRUaGVtZUZvbGRlciA9IHNraXBMb2FkZXIgKyAndGhlbWVzLycgKyBiYXNlbmFtZSh0aGVtZUZvbGRlcik7XG4gICAgICBsb2dnZXIuZGVidWcoXG4gICAgICAgICdVcGRhdGluZyB1cmwgZm9yIGZpbGUnLFxuICAgICAgICBcIidcIiArIHJlcGxhY2UgKyBmaWxlVXJsICsgXCInXCIsXG4gICAgICAgICd0byB1c2UnLFxuICAgICAgICBcIidcIiArIGZyb250ZW5kVGhlbWVGb2xkZXIgKyAnLycgKyBmaWxlVXJsICsgXCInXCJcbiAgICAgICk7XG4gICAgICBjb25zdCBwYXRoUmVzb2x2ZWQgPSBhYnNvbHV0ZVBhdGguc3Vic3RyaW5nKHRoZW1lRm9sZGVyLmxlbmd0aCkucmVwbGFjZSgvXFxcXC9nLCAnLycpO1xuXG4gICAgICAvLyBrZWVwIHRoZSB1cmwgdGhlIHNhbWUgZXhjZXB0IHJlcGxhY2UgdGhlIC4vIG9yIC4uLyB0byB0aGVtZXMvW3RoZW1lRm9sZGVyXVxuICAgICAgcmV0dXJuIHVybCArIChxdW90ZU1hcmsgPz8gJycpICsgZnJvbnRlbmRUaGVtZUZvbGRlciArIHBhdGhSZXNvbHZlZCArIGVuZFN0cmluZztcbiAgICB9IGVsc2UgaWYgKG9wdGlvbnMuZGV2TW9kZSkge1xuICAgICAgbG9nZ2VyLmxvZyhcIk5vIHJld3JpdGUgZm9yICdcIiwgbWF0Y2gsIFwiJyBhcyB0aGUgZmlsZSB3YXMgbm90IGZvdW5kLlwiKTtcbiAgICB9IGVsc2Uge1xuICAgICAgLy8gSW4gcHJvZHVjdGlvbiwgdGhlIGNzcyBpcyBpbiBWQUFESU4vYnVpbGQgYnV0IHRoZSB0aGVtZSBmaWxlcyBhcmUgaW4gLlxuICAgICAgcmV0dXJuIHVybCArIChxdW90ZU1hcmsgPz8gJycpICsgJy4uLy4uLycgKyBmaWxlVXJsICsgZW5kU3RyaW5nO1xuICAgIH1cbiAgICByZXR1cm4gbWF0Y2g7XG4gIH0pO1xuICByZXR1cm4gc291cmNlO1xufVxuXG5leHBvcnQgeyByZXdyaXRlQ3NzVXJscyB9O1xuIiwgIntcbiAgXCJmcm9udGVuZEZvbGRlclwiOiBcIkY6L0VjbGlwc2UgV29ya3NwYWNlL21lZ3Bici9mcm9udGVuZFwiLFxuICBcInRoZW1lRm9sZGVyXCI6IFwidGhlbWVzXCIsXG4gIFwidGhlbWVSZXNvdXJjZUZvbGRlclwiOiBcIkY6L0VjbGlwc2UgV29ya3NwYWNlL21lZ3Bici9mcm9udGVuZC9nZW5lcmF0ZWQvamFyLXJlc291cmNlc1wiLFxuICBcInN0YXRpY091dHB1dFwiOiBcIkY6L0VjbGlwc2UgV29ya3NwYWNlL21lZ3Bici90YXJnZXQvY2xhc3Nlcy9NRVRBLUlORi9WQUFESU4vd2ViYXBwL1ZBQURJTi9zdGF0aWNcIixcbiAgXCJnZW5lcmF0ZWRGb2xkZXJcIjogXCJnZW5lcmF0ZWRcIixcbiAgXCJzdGF0c091dHB1dFwiOiBcIkY6XFxcXEVjbGlwc2UgV29ya3NwYWNlXFxcXG1lZ3BiclxcXFx0YXJnZXRcXFxcY2xhc3Nlc1xcXFxNRVRBLUlORlxcXFxWQUFESU5cXFxcY29uZmlnXCIsXG4gIFwiZnJvbnRlbmRCdW5kbGVPdXRwdXRcIjogXCJGOlxcXFxFY2xpcHNlIFdvcmtzcGFjZVxcXFxtZWdwYnJcXFxcdGFyZ2V0XFxcXGNsYXNzZXNcXFxcTUVUQS1JTkZcXFxcVkFBRElOXFxcXHdlYmFwcFwiLFxuICBcImRldkJ1bmRsZU91dHB1dFwiOiBcIkY6L0VjbGlwc2UgV29ya3NwYWNlL21lZ3Bici9zcmMvbWFpbi9kZXYtYnVuZGxlL3dlYmFwcFwiLFxuICBcImRldkJ1bmRsZVN0YXRzT3V0cHV0XCI6IFwiRjovRWNsaXBzZSBXb3Jrc3BhY2UvbWVncGJyL3NyYy9tYWluL2Rldi1idW5kbGUvY29uZmlnXCIsXG4gIFwiamFyUmVzb3VyY2VzRm9sZGVyXCI6IFwiRjovRWNsaXBzZSBXb3Jrc3BhY2UvbWVncGJyL2Zyb250ZW5kL2dlbmVyYXRlZC9qYXItcmVzb3VyY2VzXCIsXG4gIFwidGhlbWVOYW1lXCI6IFwibWVncGJyXCIsXG4gIFwiY2xpZW50U2VydmljZVdvcmtlclNvdXJjZVwiOiBcIkY6XFxcXEVjbGlwc2UgV29ya3NwYWNlXFxcXG1lZ3BiclxcXFx0YXJnZXRcXFxcc3cudHNcIixcbiAgXCJwd2FFbmFibGVkXCI6IHRydWUsXG4gIFwib2ZmbGluZUVuYWJsZWRcIjogdHJ1ZSxcbiAgXCJvZmZsaW5lUGF0aFwiOiBcIicuJ1wiXG59IiwgImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJGOlxcXFxFY2xpcHNlIFdvcmtzcGFjZVxcXFxtZWdwYnJcXFxcdGFyZ2V0XFxcXHBsdWdpbnNcXFxccm9sbHVwLXBsdWdpbi1wb3N0Y3NzLWxpdC1jdXN0b21cIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkY6XFxcXEVjbGlwc2UgV29ya3NwYWNlXFxcXG1lZ3BiclxcXFx0YXJnZXRcXFxccGx1Z2luc1xcXFxyb2xsdXAtcGx1Z2luLXBvc3Rjc3MtbGl0LWN1c3RvbVxcXFxyb2xsdXAtcGx1Z2luLXBvc3Rjc3MtbGl0LmpzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9GOi9FY2xpcHNlJTIwV29ya3NwYWNlL21lZ3Bici90YXJnZXQvcGx1Z2lucy9yb2xsdXAtcGx1Z2luLXBvc3Rjc3MtbGl0LWN1c3RvbS9yb2xsdXAtcGx1Z2luLXBvc3Rjc3MtbGl0LmpzXCI7LyoqXG4gKiBNSVQgTGljZW5zZVxuXG5Db3B5cmlnaHQgKGMpIDIwMTkgVW1iZXJ0byBQZXBhdG9cblxuUGVybWlzc2lvbiBpcyBoZXJlYnkgZ3JhbnRlZCwgZnJlZSBvZiBjaGFyZ2UsIHRvIGFueSBwZXJzb24gb2J0YWluaW5nIGEgY29weVxub2YgdGhpcyBzb2Z0d2FyZSBhbmQgYXNzb2NpYXRlZCBkb2N1bWVudGF0aW9uIGZpbGVzICh0aGUgXCJTb2Z0d2FyZVwiKSwgdG8gZGVhbFxuaW4gdGhlIFNvZnR3YXJlIHdpdGhvdXQgcmVzdHJpY3Rpb24sIGluY2x1ZGluZyB3aXRob3V0IGxpbWl0YXRpb24gdGhlIHJpZ2h0c1xudG8gdXNlLCBjb3B5LCBtb2RpZnksIG1lcmdlLCBwdWJsaXNoLCBkaXN0cmlidXRlLCBzdWJsaWNlbnNlLCBhbmQvb3Igc2VsbFxuY29waWVzIG9mIHRoZSBTb2Z0d2FyZSwgYW5kIHRvIHBlcm1pdCBwZXJzb25zIHRvIHdob20gdGhlIFNvZnR3YXJlIGlzXG5mdXJuaXNoZWQgdG8gZG8gc28sIHN1YmplY3QgdG8gdGhlIGZvbGxvd2luZyBjb25kaXRpb25zOlxuXG5UaGUgYWJvdmUgY29weXJpZ2h0IG5vdGljZSBhbmQgdGhpcyBwZXJtaXNzaW9uIG5vdGljZSBzaGFsbCBiZSBpbmNsdWRlZCBpbiBhbGxcbmNvcGllcyBvciBzdWJzdGFudGlhbCBwb3J0aW9ucyBvZiB0aGUgU29mdHdhcmUuXG5cblRIRSBTT0ZUV0FSRSBJUyBQUk9WSURFRCBcIkFTIElTXCIsIFdJVEhPVVQgV0FSUkFOVFkgT0YgQU5ZIEtJTkQsIEVYUFJFU1MgT1JcbklNUExJRUQsIElOQ0xVRElORyBCVVQgTk9UIExJTUlURUQgVE8gVEhFIFdBUlJBTlRJRVMgT0YgTUVSQ0hBTlRBQklMSVRZLFxuRklUTkVTUyBGT1IgQSBQQVJUSUNVTEFSIFBVUlBPU0UgQU5EIE5PTklORlJJTkdFTUVOVC4gSU4gTk8gRVZFTlQgU0hBTEwgVEhFXG5BVVRIT1JTIE9SIENPUFlSSUdIVCBIT0xERVJTIEJFIExJQUJMRSBGT1IgQU5ZIENMQUlNLCBEQU1BR0VTIE9SIE9USEVSXG5MSUFCSUxJVFksIFdIRVRIRVIgSU4gQU4gQUNUSU9OIE9GIENPTlRSQUNULCBUT1JUIE9SIE9USEVSV0lTRSwgQVJJU0lORyBGUk9NLFxuT1VUIE9GIE9SIElOIENPTk5FQ1RJT04gV0lUSCBUSEUgU09GVFdBUkUgT1IgVEhFIFVTRSBPUiBPVEhFUiBERUFMSU5HUyBJTiBUSEVcblNPRlRXQVJFLlxuICovXG4vLyBUaGlzIGlzIGh0dHBzOi8vZ2l0aHViLmNvbS91bWJvcGVwYXRvL3JvbGx1cC1wbHVnaW4tcG9zdGNzcy1saXQgMi4wLjAgKyBodHRwczovL2dpdGh1Yi5jb20vdW1ib3BlcGF0by9yb2xsdXAtcGx1Z2luLXBvc3Rjc3MtbGl0L3B1bGwvNTRcbi8vIHRvIG1ha2UgaXQgd29yayB3aXRoIFZpdGUgM1xuLy8gT25jZSAvIGlmIGh0dHBzOi8vZ2l0aHViLmNvbS91bWJvcGVwYXRvL3JvbGx1cC1wbHVnaW4tcG9zdGNzcy1saXQvcHVsbC81NCBpcyBtZXJnZWQgdGhpcyBzaG91bGQgYmUgcmVtb3ZlZCBhbmQgcm9sbHVwLXBsdWdpbi1wb3N0Y3NzLWxpdCBzaG91bGQgYmUgdXNlZCBpbnN0ZWFkXG5cbmltcG9ydCB7IGNyZWF0ZUZpbHRlciB9IGZyb20gJ0Byb2xsdXAvcGx1Z2ludXRpbHMnO1xuaW1wb3J0IHRyYW5zZm9ybUFzdCBmcm9tICd0cmFuc2Zvcm0tYXN0JztcblxuY29uc3QgYXNzZXRVcmxSRSA9IC9fX1ZJVEVfQVNTRVRfXyhbYS16XFxkXXs4fSlfXyg/OlxcJF8oLio/KV9fKT8vZztcblxuY29uc3QgZXNjYXBlID0gKHN0cikgPT5cbiAgc3RyXG4gICAgLnJlcGxhY2UoYXNzZXRVcmxSRSwgJyR7dW5zYWZlQ1NTVGFnKFwiX19WSVRFX0FTU0VUX18kMV9fJDJcIil9JylcbiAgICAucmVwbGFjZSgvYC9nLCAnXFxcXGAnKVxuICAgIC5yZXBsYWNlKC9cXFxcKD8hYCkvZywgJ1xcXFxcXFxcJyk7XG5cbmV4cG9ydCBkZWZhdWx0IGZ1bmN0aW9uIHBvc3Rjc3NMaXQob3B0aW9ucyA9IHt9KSB7XG4gIGNvbnN0IGRlZmF1bHRPcHRpb25zID0ge1xuICAgIGluY2x1ZGU6ICcqKi8qLntjc3Msc3NzLHBjc3Msc3R5bCxzdHlsdXMsc2FzcyxzY3NzLGxlc3N9JyxcbiAgICBleGNsdWRlOiBudWxsLFxuICAgIGltcG9ydFBhY2thZ2U6ICdsaXQnXG4gIH07XG5cbiAgY29uc3Qgb3B0cyA9IHsgLi4uZGVmYXVsdE9wdGlvbnMsIC4uLm9wdGlvbnMgfTtcbiAgY29uc3QgZmlsdGVyID0gY3JlYXRlRmlsdGVyKG9wdHMuaW5jbHVkZSwgb3B0cy5leGNsdWRlKTtcblxuICByZXR1cm4ge1xuICAgIG5hbWU6ICdwb3N0Y3NzLWxpdCcsXG4gICAgZW5mb3JjZTogJ3Bvc3QnLFxuICAgIHRyYW5zZm9ybShjb2RlLCBpZCkge1xuICAgICAgaWYgKCFmaWx0ZXIoaWQpKSByZXR1cm47XG4gICAgICBjb25zdCBhc3QgPSB0aGlzLnBhcnNlKGNvZGUsIHt9KTtcbiAgICAgIC8vIGV4cG9ydCBkZWZhdWx0IGNvbnN0IGNzcztcbiAgICAgIGxldCBkZWZhdWx0RXhwb3J0TmFtZTtcblxuICAgICAgLy8gZXhwb3J0IGRlZmF1bHQgJy4uLic7XG4gICAgICBsZXQgaXNEZWNsYXJhdGlvbkxpdGVyYWwgPSBmYWxzZTtcbiAgICAgIGNvbnN0IG1hZ2ljU3RyaW5nID0gdHJhbnNmb3JtQXN0KGNvZGUsIHsgYXN0OiBhc3QgfSwgKG5vZGUpID0+IHtcbiAgICAgICAgaWYgKG5vZGUudHlwZSA9PT0gJ0V4cG9ydERlZmF1bHREZWNsYXJhdGlvbicpIHtcbiAgICAgICAgICBkZWZhdWx0RXhwb3J0TmFtZSA9IG5vZGUuZGVjbGFyYXRpb24ubmFtZTtcblxuICAgICAgICAgIGlzRGVjbGFyYXRpb25MaXRlcmFsID0gbm9kZS5kZWNsYXJhdGlvbi50eXBlID09PSAnTGl0ZXJhbCc7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuXG4gICAgICBpZiAoIWRlZmF1bHRFeHBvcnROYW1lICYmICFpc0RlY2xhcmF0aW9uTGl0ZXJhbCkge1xuICAgICAgICByZXR1cm47XG4gICAgICB9XG4gICAgICBtYWdpY1N0cmluZy53YWxrKChub2RlKSA9PiB7XG4gICAgICAgIGlmIChkZWZhdWx0RXhwb3J0TmFtZSAmJiBub2RlLnR5cGUgPT09ICdWYXJpYWJsZURlY2xhcmF0aW9uJykge1xuICAgICAgICAgIGNvbnN0IGV4cG9ydGVkVmFyID0gbm9kZS5kZWNsYXJhdGlvbnMuZmluZCgoZCkgPT4gZC5pZC5uYW1lID09PSBkZWZhdWx0RXhwb3J0TmFtZSk7XG4gICAgICAgICAgaWYgKGV4cG9ydGVkVmFyKSB7XG4gICAgICAgICAgICBleHBvcnRlZFZhci5pbml0LmVkaXQudXBkYXRlKGBjc3NUYWdcXGAke2VzY2FwZShleHBvcnRlZFZhci5pbml0LnZhbHVlKX1cXGBgKTtcbiAgICAgICAgICB9XG4gICAgICAgIH1cblxuICAgICAgICBpZiAoaXNEZWNsYXJhdGlvbkxpdGVyYWwgJiYgbm9kZS50eXBlID09PSAnRXhwb3J0RGVmYXVsdERlY2xhcmF0aW9uJykge1xuICAgICAgICAgIG5vZGUuZGVjbGFyYXRpb24uZWRpdC51cGRhdGUoYGNzc1RhZ1xcYCR7ZXNjYXBlKG5vZGUuZGVjbGFyYXRpb24udmFsdWUpfVxcYGApO1xuICAgICAgICB9XG4gICAgICB9KTtcbiAgICAgIG1hZ2ljU3RyaW5nLnByZXBlbmQoYGltcG9ydCB7Y3NzIGFzIGNzc1RhZywgdW5zYWZlQ1NTIGFzIHVuc2FmZUNTU1RhZ30gZnJvbSAnJHtvcHRzLmltcG9ydFBhY2thZ2V9JztcXG5gKTtcbiAgICAgIHJldHVybiB7XG4gICAgICAgIGNvZGU6IG1hZ2ljU3RyaW5nLnRvU3RyaW5nKCksXG4gICAgICAgIG1hcDogbWFnaWNTdHJpbmcuZ2VuZXJhdGVNYXAoe1xuICAgICAgICAgIGhpcmVzOiB0cnVlXG4gICAgICAgIH0pXG4gICAgICB9O1xuICAgIH1cbiAgfTtcbn07XG4iLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkY6XFxcXEVjbGlwc2UgV29ya3NwYWNlXFxcXG1lZ3BiclwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiRjpcXFxcRWNsaXBzZSBXb3Jrc3BhY2VcXFxcbWVncGJyXFxcXHZpdGUuY29uZmlnLnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9GOi9FY2xpcHNlJTIwV29ya3NwYWNlL21lZ3Bici92aXRlLmNvbmZpZy50c1wiO2ltcG9ydCB7IFVzZXJDb25maWdGbiB9IGZyb20gJ3ZpdGUnO1xuaW1wb3J0IHsgb3ZlcnJpZGVWYWFkaW5Db25maWcgfSBmcm9tICcuL3ZpdGUuZ2VuZXJhdGVkJztcblxuY29uc3QgY3VzdG9tQ29uZmlnOiBVc2VyQ29uZmlnRm4gPSAoZW52KSA9PiAoe1xuICAvLyBIZXJlIHlvdSBjYW4gYWRkIGN1c3RvbSBWaXRlIHBhcmFtZXRlcnNcbiAgLy8gaHR0cHM6Ly92aXRlanMuZGV2L2NvbmZpZy9cbn0pO1xuXG5leHBvcnQgZGVmYXVsdCBvdmVycmlkZVZhYWRpbkNvbmZpZyhjdXN0b21Db25maWcpO1xuXG5cblxuIl0sCiAgIm1hcHBpbmdzIjogIjtBQU1BLE9BQU8sVUFBVTtBQUNqQixTQUFTLGNBQUFBLGFBQVksYUFBQUMsWUFBVyxlQUFBQyxjQUFhLGdCQUFBQyxlQUFjLGlCQUFBQyxzQkFBcUI7QUFDaEYsU0FBUyxrQkFBa0I7QUFDM0IsWUFBWSxTQUFTOzs7QUNXckIsU0FBUyxjQUFBQyxhQUFZLGdCQUFBQyxxQkFBb0I7QUFDekMsU0FBUyxXQUFBQyxnQkFBZTs7O0FDRHhCLFNBQVMsWUFBQUMsaUJBQWdCO0FBQ3pCLFNBQVMsV0FBQUMsVUFBUyxZQUFBQyxpQkFBZ0I7QUFDbEMsU0FBUyxjQUFBQyxhQUFZLGNBQWMscUJBQXFCOzs7QUNGeEQsU0FBUyxhQUFhLFVBQVUsV0FBVyxZQUFZLG9CQUFvQjtBQUMzRSxTQUFTLFNBQVMsVUFBVSxVQUFVLGVBQWU7QUFDckQsU0FBUyxnQkFBZ0I7QUFFekIsSUFBTSx3QkFBd0IsQ0FBQyxRQUFRLE9BQU8sT0FBTztBQVdyRCxTQUFTLG1CQUFtQkMsY0FBYSxpQ0FBaUMsUUFBUTtBQUNoRixRQUFNLDBCQUEwQixRQUFRLGlDQUFpQyxVQUFVLFNBQVNBLFlBQVcsQ0FBQztBQUN4RyxRQUFNLGFBQWEsZUFBZUEsY0FBYSxNQUFNO0FBR3JELE1BQUksV0FBVyxNQUFNLFNBQVMsR0FBRztBQUMvQixjQUFVLHlCQUF5QixFQUFFLFdBQVcsS0FBSyxDQUFDO0FBRXRELGVBQVcsWUFBWSxRQUFRLENBQUMsY0FBYztBQUM1QyxZQUFNLG9CQUFvQixTQUFTQSxjQUFhLFNBQVM7QUFDekQsWUFBTSxrQkFBa0IsUUFBUSx5QkFBeUIsaUJBQWlCO0FBRTFFLGdCQUFVLGlCQUFpQixFQUFFLFdBQVcsS0FBSyxDQUFDO0FBQUEsSUFDaEQsQ0FBQztBQUVELGVBQVcsTUFBTSxRQUFRLENBQUMsU0FBUztBQUNqQyxZQUFNLGVBQWUsU0FBU0EsY0FBYSxJQUFJO0FBQy9DLFlBQU0sYUFBYSxRQUFRLHlCQUF5QixZQUFZO0FBQ2hFLDhCQUF3QixNQUFNLFlBQVksTUFBTTtBQUFBLElBQ2xELENBQUM7QUFBQSxFQUNIO0FBQ0Y7QUFZQSxTQUFTLGVBQWUsY0FBYyxRQUFRO0FBQzVDLFFBQU0sYUFBYSxFQUFFLGFBQWEsQ0FBQyxHQUFHLE9BQU8sQ0FBQyxFQUFFO0FBQ2hELFNBQU8sTUFBTSxzQkFBc0IsWUFBWSxZQUFZLENBQUM7QUFDNUQsY0FBWSxZQUFZLEVBQUUsUUFBUSxDQUFDLFNBQVM7QUFDMUMsVUFBTSxhQUFhLFFBQVEsY0FBYyxJQUFJO0FBQzdDLFFBQUk7QUFDRixVQUFJLFNBQVMsVUFBVSxFQUFFLFlBQVksR0FBRztBQUN0QyxlQUFPLE1BQU0sMkJBQTJCLFVBQVU7QUFDbEQsY0FBTSxTQUFTLGVBQWUsWUFBWSxNQUFNO0FBQ2hELFlBQUksT0FBTyxNQUFNLFNBQVMsR0FBRztBQUMzQixxQkFBVyxZQUFZLEtBQUssVUFBVTtBQUN0QyxpQkFBTyxNQUFNLG9CQUFvQixVQUFVO0FBQzNDLHFCQUFXLFlBQVksS0FBSyxNQUFNLFdBQVcsYUFBYSxPQUFPLFdBQVc7QUFDNUUscUJBQVcsTUFBTSxLQUFLLE1BQU0sV0FBVyxPQUFPLE9BQU8sS0FBSztBQUFBLFFBQzVEO0FBQUEsTUFDRixXQUFXLENBQUMsc0JBQXNCLFNBQVMsUUFBUSxVQUFVLENBQUMsR0FBRztBQUMvRCxlQUFPLE1BQU0sZUFBZSxVQUFVO0FBQ3RDLG1CQUFXLE1BQU0sS0FBSyxVQUFVO0FBQUEsTUFDbEM7QUFBQSxJQUNGLFNBQVMsT0FBTztBQUNkLDRCQUFzQixZQUFZLE9BQU8sTUFBTTtBQUFBLElBQ2pEO0FBQUEsRUFDRixDQUFDO0FBQ0QsU0FBTztBQUNUO0FBOEJBLFNBQVMsaUJBQWlCLFdBQVcsaUJBQWlCLGlDQUFpQyxRQUFRO0FBQzdGLFFBQU0sU0FBUyxnQkFBZ0IsUUFBUTtBQUN2QyxNQUFJLENBQUMsUUFBUTtBQUNYLFdBQU8sTUFBTSxrREFBa0Q7QUFDL0Q7QUFBQSxFQUNGO0FBRUEsWUFBVSxpQ0FBaUM7QUFBQSxJQUN6QyxXQUFXO0FBQUEsRUFDYixDQUFDO0FBQ0QsUUFBTSxpQkFBaUIsYUFBYSxPQUFPLEtBQUssTUFBTSxDQUFDO0FBQ3ZELE1BQUksZUFBZSxTQUFTLEdBQUc7QUFDN0IsVUFBTTtBQUFBLE1BQ0osMEJBQ0UsZUFBZSxLQUFLLE1BQU0sSUFDMUI7QUFBQSxJQUVKO0FBQUEsRUFDRjtBQUNBLFNBQU8sS0FBSyxNQUFNLEVBQUUsUUFBUSxDQUFDLFdBQVc7QUFDdEMsVUFBTSxZQUFZLE9BQU8sTUFBTTtBQUMvQixXQUFPLEtBQUssU0FBUyxFQUFFLFFBQVEsQ0FBQyxhQUFhO0FBQzNDLFlBQU0sY0FBYyxRQUFRLGlCQUFpQixRQUFRLFFBQVE7QUFDN0QsWUFBTSxRQUFRLFNBQVMsYUFBYSxFQUFFLE9BQU8sS0FBSyxDQUFDO0FBQ25ELFlBQU0sZUFBZSxRQUFRLGlDQUFpQyxVQUFVLFdBQVcsVUFBVSxRQUFRLENBQUM7QUFFdEcsZ0JBQVUsY0FBYztBQUFBLFFBQ3RCLFdBQVc7QUFBQSxNQUNiLENBQUM7QUFDRCxZQUFNLFFBQVEsQ0FBQyxTQUFTO0FBQ3RCLGNBQU0sYUFBYSxRQUFRLGNBQWMsU0FBUyxJQUFJLENBQUM7QUFDdkQsZ0NBQXdCLE1BQU0sWUFBWSxNQUFNO0FBQUEsTUFDbEQsQ0FBQztBQUFBLElBQ0gsQ0FBQztBQUFBLEVBQ0gsQ0FBQztBQUNIO0FBRUEsU0FBUyxhQUFhLFNBQVM7QUFDN0IsUUFBTSxVQUFVLENBQUM7QUFFakIsVUFBUSxRQUFRLENBQUMsV0FBVztBQUMxQixRQUFJLENBQUMsV0FBVyxRQUFRLGlCQUFpQixNQUFNLENBQUMsR0FBRztBQUNqRCxjQUFRLEtBQUssTUFBTTtBQUFBLElBQ3JCO0FBQUEsRUFDRixDQUFDO0FBRUQsU0FBTztBQUNUO0FBU0EsU0FBUyx3QkFBd0IsWUFBWSxZQUFZLFFBQVE7QUFDL0QsTUFBSTtBQUNGLFFBQUksQ0FBQyxXQUFXLFVBQVUsS0FBSyxTQUFTLFVBQVUsRUFBRSxRQUFRLFNBQVMsVUFBVSxFQUFFLE9BQU87QUFDdEYsYUFBTyxNQUFNLGFBQWEsWUFBWSxNQUFNLFVBQVU7QUFDdEQsbUJBQWEsWUFBWSxVQUFVO0FBQUEsSUFDckM7QUFBQSxFQUNGLFNBQVMsT0FBTztBQUNkLDBCQUFzQixZQUFZLE9BQU8sTUFBTTtBQUFBLEVBQ2pEO0FBQ0Y7QUFLQSxTQUFTLHNCQUFzQixNQUFNLE9BQU8sUUFBUTtBQUNsRCxNQUFJLE1BQU0sU0FBUyxVQUFVO0FBQzNCLFdBQU8sS0FBSyxnQ0FBZ0MsT0FBTyx1REFBdUQ7QUFBQSxFQUM1RyxPQUFPO0FBQ0wsVUFBTTtBQUFBLEVBQ1I7QUFDRjs7O0FENUtBLElBQU0sd0JBQXdCO0FBRzlCLElBQU0sc0JBQXNCO0FBRTVCLElBQU0sb0JBQW9CO0FBRTFCLElBQU0sb0JBQW9CO0FBQzFCLElBQU0sZUFBZTtBQUFBO0FBWXJCLFNBQVMsZ0JBQWdCQyxjQUFhLFdBQVcsaUJBQWlCLFNBQVM7QUFDekUsUUFBTSxpQkFBaUIsQ0FBQyxRQUFRO0FBQ2hDLFFBQU0saUNBQWlDLENBQUMsUUFBUTtBQUNoRCxRQUFNLGVBQWUsUUFBUTtBQUM3QixRQUFNLFNBQVNDLFNBQVFELGNBQWEsaUJBQWlCO0FBQ3JELFFBQU0sa0JBQWtCQyxTQUFRRCxjQUFhLG1CQUFtQjtBQUNoRSxRQUFNLHVCQUF1QixnQkFBZ0Isd0JBQXdCO0FBQ3JFLFFBQU0saUJBQWlCLFdBQVcsWUFBWTtBQUM5QyxRQUFNLHFCQUFxQixXQUFXLFlBQVk7QUFDbEQsUUFBTSxnQkFBZ0IsV0FBVyxZQUFZO0FBRTdDLE1BQUksbUJBQW1CO0FBQ3ZCLE1BQUksc0JBQXNCO0FBQzFCLE1BQUksd0JBQXdCO0FBQzVCLE1BQUk7QUFFSixNQUFJLHNCQUFzQjtBQUN4QixzQkFBa0JFLFVBQVMsU0FBUztBQUFBLE1BQ2xDLEtBQUtELFNBQVFELGNBQWEscUJBQXFCO0FBQUEsTUFDL0MsT0FBTztBQUFBLElBQ1QsQ0FBQztBQUVELFFBQUksZ0JBQWdCLFNBQVMsR0FBRztBQUM5QiwrQkFDRTtBQUFBLElBQ0o7QUFBQSxFQUNGO0FBRUEsTUFBSSxnQkFBZ0IsUUFBUTtBQUMxQix3QkFBb0IseURBQXlELGdCQUFnQixNQUFNO0FBQUE7QUFBQSxFQUNyRztBQUVBLHNCQUFvQjtBQUFBO0FBQ3BCLHNCQUFvQixhQUFhLGtCQUFrQjtBQUFBO0FBRW5ELHNCQUFvQjtBQUFBO0FBQ3BCLFFBQU0sVUFBVSxDQUFDO0FBQ2pCLFFBQU0sc0JBQXNCLENBQUM7QUFDN0IsUUFBTSxvQkFBb0IsQ0FBQztBQUMzQixRQUFNLGdCQUFnQixDQUFDO0FBQ3ZCLFFBQU0sZ0JBQWdCLENBQUM7QUFDdkIsUUFBTSxtQkFBbUIsQ0FBQztBQUMxQixRQUFNLGNBQWMsZ0JBQWdCLFNBQVMsOEJBQThCO0FBQzNFLFFBQU0sMEJBQTBCLGdCQUFnQixTQUM1QyxtQkFBbUIsZ0JBQWdCLE1BQU07QUFBQSxJQUN6QztBQUVKLFFBQU0sa0JBQWtCLGtCQUFrQixZQUFZO0FBQ3RELFFBQU0sY0FBYztBQUNwQixRQUFNLGdCQUFnQixrQkFBa0I7QUFDeEMsUUFBTSxtQkFBbUIsa0JBQWtCO0FBRTNDLE1BQUksQ0FBQ0csWUFBVyxNQUFNLEdBQUc7QUFDdkIsUUFBSSxnQkFBZ0I7QUFDbEIsWUFBTSxJQUFJLE1BQU0saURBQWlELFNBQVMsZ0JBQWdCSCxZQUFXLEdBQUc7QUFBQSxJQUMxRztBQUNBO0FBQUEsTUFDRTtBQUFBLE1BQ0E7QUFBQSxNQUNBO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFHQSxNQUFJLFdBQVdJLFVBQVMsTUFBTTtBQUM5QixNQUFJLFdBQVcsVUFBVSxRQUFRO0FBR2pDLFFBQU0sY0FBYyxnQkFBZ0IsZUFBZSxDQUFDLFNBQVMsWUFBWTtBQUN6RSxNQUFJLGFBQWE7QUFDZixnQkFBWSxRQUFRLENBQUMsZUFBZTtBQUNsQyxjQUFRLEtBQUssWUFBWSxVQUFVLHVDQUF1QyxVQUFVO0FBQUEsQ0FBUztBQUM3RixVQUFJLGVBQWUsYUFBYSxlQUFlLFdBQVcsZUFBZSxnQkFBZ0IsZUFBZSxTQUFTO0FBRS9HLGdCQUFRLEtBQUssc0NBQXNDLFVBQVU7QUFBQSxDQUFnQjtBQUFBLE1BQy9FO0FBQUEsSUFDRixDQUFDO0FBRUQsZ0JBQVksUUFBUSxDQUFDLGVBQWU7QUFFbEMsb0JBQWMsS0FBSyxpQ0FBaUMsVUFBVTtBQUFBLENBQWlDO0FBQUEsSUFDakcsQ0FBQztBQUFBLEVBQ0g7QUFHQSxNQUFJLGdDQUFnQztBQUNsQyxzQkFBa0IsS0FBSyx1QkFBdUI7QUFDOUMsc0JBQWtCLEtBQUssa0JBQWtCLFNBQVMsSUFBSSxRQUFRO0FBQUEsQ0FBTTtBQUVwRSxZQUFRLEtBQUssVUFBVSxRQUFRLGlCQUFpQixTQUFTLElBQUksUUFBUTtBQUFBLENBQWE7QUFDbEYsa0JBQWMsS0FBSyxpQ0FBaUMsUUFBUTtBQUFBLEtBQWtDO0FBQUEsRUFDaEc7QUFDQSxNQUFJRCxZQUFXLGVBQWUsR0FBRztBQUMvQixlQUFXQyxVQUFTLGVBQWU7QUFDbkMsZUFBVyxVQUFVLFFBQVE7QUFFN0IsUUFBSSxnQ0FBZ0M7QUFDbEMsd0JBQWtCLEtBQUssa0JBQWtCLFNBQVMsSUFBSSxRQUFRO0FBQUEsQ0FBTTtBQUVwRSxjQUFRLEtBQUssVUFBVSxRQUFRLGlCQUFpQixTQUFTLElBQUksUUFBUTtBQUFBLENBQWE7QUFDbEYsb0JBQWMsS0FBSyxpQ0FBaUMsUUFBUTtBQUFBLEtBQW1DO0FBQUEsSUFDakc7QUFBQSxFQUNGO0FBRUEsTUFBSSxJQUFJO0FBQ1IsTUFBSSxnQkFBZ0IsYUFBYTtBQUMvQixVQUFNLGlCQUFpQixhQUFhLGdCQUFnQixXQUFXO0FBQy9ELFFBQUksZUFBZSxTQUFTLEdBQUc7QUFDN0IsWUFBTTtBQUFBLFFBQ0osbUNBQ0UsZUFBZSxLQUFLLE1BQU0sSUFDMUI7QUFBQSxNQUVKO0FBQUEsSUFDRjtBQUNBLG9CQUFnQixZQUFZLFFBQVEsQ0FBQyxjQUFjO0FBQ2pELFlBQU1DLFlBQVcsV0FBVztBQUM1QixjQUFRLEtBQUssVUFBVUEsU0FBUSxVQUFVLFNBQVM7QUFBQSxDQUFhO0FBRy9ELG9CQUFjLEtBQUs7QUFBQSx3Q0FDZUEsU0FBUTtBQUFBO0FBQUEsS0FDcEM7QUFDTixvQkFBYztBQUFBLFFBQ1osaUNBQWlDQSxTQUFRLGlCQUFpQixpQkFBaUI7QUFBQTtBQUFBLE1BQzdFO0FBQUEsSUFDRixDQUFDO0FBQUEsRUFDSDtBQUNBLE1BQUksZ0JBQWdCLFdBQVc7QUFDN0IsVUFBTSxpQkFBaUIsYUFBYSxnQkFBZ0IsU0FBUztBQUM3RCxRQUFJLGVBQWUsU0FBUyxHQUFHO0FBQzdCLFlBQU07QUFBQSxRQUNKLG1DQUNFLGVBQWUsS0FBSyxNQUFNLElBQzFCO0FBQUEsTUFFSjtBQUFBLElBQ0Y7QUFDQSxvQkFBZ0IsVUFBVSxRQUFRLENBQUMsWUFBWTtBQUM3QyxZQUFNQSxZQUFXLFdBQVc7QUFDNUIsd0JBQWtCLEtBQUssV0FBVyxPQUFPO0FBQUEsQ0FBTTtBQUMvQyxjQUFRLEtBQUssVUFBVUEsU0FBUSxVQUFVLE9BQU87QUFBQSxDQUFhO0FBQzdELG9CQUFjLEtBQUssaUNBQWlDQSxTQUFRLGlCQUFpQixpQkFBaUI7QUFBQSxDQUFnQjtBQUFBLElBQ2hILENBQUM7QUFBQSxFQUNIO0FBRUEsTUFBSSxzQkFBc0I7QUFDeEIsb0JBQWdCLFFBQVEsQ0FBQyxpQkFBaUI7QUFDeEMsWUFBTUMsWUFBV0YsVUFBUyxZQUFZO0FBQ3RDLFlBQU0sTUFBTUUsVUFBUyxRQUFRLFFBQVEsRUFBRTtBQUN2QyxZQUFNRCxZQUFXLFVBQVVDLFNBQVE7QUFDbkMsMEJBQW9CO0FBQUEsUUFDbEIsVUFBVUQsU0FBUSxpQkFBaUIsU0FBUyxJQUFJLHFCQUFxQixJQUFJQyxTQUFRO0FBQUE7QUFBQSxNQUNuRjtBQUVBLFlBQU0sa0JBQWtCO0FBQUEsV0FDbkIsR0FBRztBQUFBLG9CQUNNRCxTQUFRO0FBQUE7QUFBQTtBQUd0Qix1QkFBaUIsS0FBSyxlQUFlO0FBQUEsSUFDdkMsQ0FBQztBQUFBLEVBQ0g7QUFFQSxzQkFBb0IsUUFBUSxLQUFLLEVBQUU7QUFJbkMsUUFBTSxpQkFBaUI7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQSxRQU9qQixjQUFjLEtBQUssRUFBRSxDQUFDO0FBQUE7QUFBQSxNQUV4QixXQUFXO0FBQUEsTUFDWCxjQUFjLEtBQUssRUFBRSxDQUFDO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBVTFCLDJCQUF5QjtBQUFBLEVBQ3pCLG9CQUFvQixLQUFLLEVBQUUsQ0FBQztBQUFBO0FBQUEsaUJBRWIsZ0JBQWdCO0FBQUEsSUFDN0IsaUJBQWlCLEtBQUssRUFBRSxDQUFDO0FBQUEsY0FDZixnQkFBZ0I7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFXNUIsc0JBQW9CO0FBQ3BCLHNCQUFvQjtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBd0JwQix5QkFBdUI7QUFBQSxFQUN2QixrQkFBa0IsS0FBSyxFQUFFLENBQUM7QUFBQTtBQUcxQixpQkFBZUosU0FBUSxjQUFjLGNBQWMsR0FBRyxtQkFBbUI7QUFDekUsaUJBQWVBLFNBQVEsY0FBYyxhQUFhLEdBQUcsZ0JBQWdCO0FBQ3JFLGlCQUFlQSxTQUFRLGNBQWMsa0JBQWtCLEdBQUcscUJBQXFCO0FBQ2pGO0FBRUEsU0FBUyxlQUFlLE1BQU0sTUFBTTtBQUNsQyxNQUFJLENBQUNFLFlBQVcsSUFBSSxLQUFLLGFBQWEsTUFBTSxFQUFFLFVBQVUsUUFBUSxDQUFDLE1BQU0sTUFBTTtBQUMzRSxrQkFBYyxNQUFNLElBQUk7QUFBQSxFQUMxQjtBQUNGO0FBUUEsU0FBUyxVQUFVLEtBQUs7QUFDdEIsU0FBTyxJQUNKLFFBQVEsdUJBQXVCLFNBQVUsTUFBTSxPQUFPO0FBQ3JELFdBQU8sVUFBVSxJQUFJLEtBQUssWUFBWSxJQUFJLEtBQUssWUFBWTtBQUFBLEVBQzdELENBQUMsRUFDQSxRQUFRLFFBQVEsRUFBRSxFQUNsQixRQUFRLFVBQVUsRUFBRTtBQUN6Qjs7O0FEclJBLElBQU0sWUFBWTtBQUVsQixJQUFJLGdCQUFnQjtBQUNwQixJQUFJLGlCQUFpQjtBQVlyQixTQUFTLHNCQUFzQixTQUFTLFFBQVE7QUFDOUMsUUFBTSxZQUFZLGlCQUFpQixRQUFRLHVCQUF1QjtBQUNsRSxNQUFJLFdBQVc7QUFDYixRQUFJLENBQUMsaUJBQWlCLENBQUMsZ0JBQWdCO0FBQ3JDLHVCQUFpQjtBQUFBLElBQ25CLFdBQ0csaUJBQWlCLGtCQUFrQixhQUFhLG1CQUFtQixhQUNuRSxDQUFDLGlCQUFpQixtQkFBbUIsV0FDdEM7QUFRQSxZQUFNLFVBQVUsMkNBQTJDLFNBQVM7QUFDcEUsWUFBTSxjQUFjO0FBQUEsMkRBQ2lDLFNBQVM7QUFBQTtBQUFBO0FBRzlELGFBQU8sS0FBSyxxRUFBcUU7QUFDakYsYUFBTyxLQUFLLE9BQU87QUFDbkIsYUFBTyxLQUFLLFdBQVc7QUFDdkIsYUFBTyxLQUFLLHFFQUFxRTtBQUFBLElBQ25GO0FBQ0Esb0JBQWdCO0FBRWhCLGtDQUE4QixXQUFXLFNBQVMsTUFBTTtBQUFBLEVBQzFELE9BQU87QUFLTCxvQkFBZ0I7QUFDaEIsV0FBTyxNQUFNLDZDQUE2QztBQUMxRCxXQUFPLE1BQU0sMkVBQTJFO0FBQUEsRUFDMUY7QUFDRjtBQVdBLFNBQVMsOEJBQThCLFdBQVcsU0FBUyxRQUFRO0FBQ2pFLE1BQUksYUFBYTtBQUNqQixXQUFTLElBQUksR0FBRyxJQUFJLFFBQVEsb0JBQW9CLFFBQVEsS0FBSztBQUMzRCxVQUFNLHFCQUFxQixRQUFRLG9CQUFvQixDQUFDO0FBQ3hELFFBQUlJLFlBQVcsa0JBQWtCLEdBQUc7QUFDbEMsYUFBTyxNQUFNLDhCQUE4QixxQkFBcUIsa0JBQWtCLFlBQVksR0FBRztBQUNqRyxZQUFNLFVBQVUsYUFBYSxXQUFXLG9CQUFvQixTQUFTLE1BQU07QUFDM0UsVUFBSSxTQUFTO0FBQ1gsWUFBSSxZQUFZO0FBQ2QsZ0JBQU0sSUFBSTtBQUFBLFlBQ1IsMkJBQ0UscUJBQ0EsWUFDQSxhQUNBO0FBQUEsVUFDSjtBQUFBLFFBQ0Y7QUFDQSxlQUFPLE1BQU0sNkJBQTZCLHFCQUFxQixHQUFHO0FBQ2xFLHFCQUFhO0FBQUEsTUFDZjtBQUFBLElBQ0Y7QUFBQSxFQUNGO0FBRUEsTUFBSUEsWUFBVyxRQUFRLG1CQUFtQixHQUFHO0FBQzNDLFFBQUksY0FBY0EsWUFBV0MsU0FBUSxRQUFRLHFCQUFxQixTQUFTLENBQUMsR0FBRztBQUM3RSxZQUFNLElBQUk7QUFBQSxRQUNSLFlBQ0UsWUFDQTtBQUFBO0FBQUEsTUFFSjtBQUFBLElBQ0Y7QUFDQSxXQUFPO0FBQUEsTUFDTCwwQ0FBMEMsUUFBUSxzQkFBc0Isa0JBQWtCLFlBQVk7QUFBQSxJQUN4RztBQUNBLGlCQUFhLFdBQVcsUUFBUSxxQkFBcUIsU0FBUyxNQUFNO0FBQ3BFLGlCQUFhO0FBQUEsRUFDZjtBQUNBLFNBQU87QUFDVDtBQW1CQSxTQUFTLGFBQWEsV0FBVyxjQUFjLFNBQVMsUUFBUTtBQUM5RCxRQUFNQyxlQUFjRCxTQUFRLGNBQWMsU0FBUztBQUNuRCxNQUFJRCxZQUFXRSxZQUFXLEdBQUc7QUFDM0IsV0FBTyxNQUFNLGdCQUFnQixXQUFXLGVBQWVBLFlBQVc7QUFFbEUsVUFBTSxrQkFBa0IsbUJBQW1CQSxZQUFXO0FBR3RELFFBQUksZ0JBQWdCLFFBQVE7QUFDMUIsWUFBTSxRQUFRLDhCQUE4QixnQkFBZ0IsUUFBUSxTQUFTLE1BQU07QUFDbkYsVUFBSSxDQUFDLE9BQU87QUFDVixjQUFNLElBQUk7QUFBQSxVQUNSLHNEQUNFLGdCQUFnQixTQUNoQjtBQUFBLFFBRUo7QUFBQSxNQUNGO0FBQUEsSUFDRjtBQUNBLHFCQUFpQixXQUFXLGlCQUFpQixRQUFRLGlDQUFpQyxNQUFNO0FBQzVGLHVCQUFtQkEsY0FBYSxRQUFRLGlDQUFpQyxNQUFNO0FBRS9FLG9CQUFnQkEsY0FBYSxXQUFXLGlCQUFpQixPQUFPO0FBQ2hFLFdBQU87QUFBQSxFQUNUO0FBQ0EsU0FBTztBQUNUO0FBRUEsU0FBUyxtQkFBbUJBLGNBQWE7QUFDdkMsUUFBTSxvQkFBb0JELFNBQVFDLGNBQWEsWUFBWTtBQUMzRCxNQUFJLENBQUNGLFlBQVcsaUJBQWlCLEdBQUc7QUFDbEMsV0FBTyxDQUFDO0FBQUEsRUFDVjtBQUNBLFFBQU0sNEJBQTRCRyxjQUFhLGlCQUFpQjtBQUNoRSxNQUFJLDBCQUEwQixXQUFXLEdBQUc7QUFDMUMsV0FBTyxDQUFDO0FBQUEsRUFDVjtBQUNBLFNBQU8sS0FBSyxNQUFNLHlCQUF5QjtBQUM3QztBQVFBLFNBQVMsaUJBQWlCLHlCQUF5QjtBQUNqRCxNQUFJLENBQUMseUJBQXlCO0FBQzVCLFVBQU0sSUFBSTtBQUFBLE1BQ1I7QUFBQSxJQUlGO0FBQUEsRUFDRjtBQUNBLFFBQU0scUJBQXFCRixTQUFRLHlCQUF5QixVQUFVO0FBQ3RFLE1BQUlELFlBQVcsa0JBQWtCLEdBQUc7QUFHbEMsVUFBTSxZQUFZLFVBQVUsS0FBS0csY0FBYSxvQkFBb0IsRUFBRSxVQUFVLE9BQU8sQ0FBQyxDQUFDLEVBQUUsQ0FBQztBQUMxRixRQUFJLENBQUMsV0FBVztBQUNkLFlBQU0sSUFBSSxNQUFNLHFDQUFxQyxxQkFBcUIsSUFBSTtBQUFBLElBQ2hGO0FBQ0EsV0FBTztBQUFBLEVBQ1QsT0FBTztBQUNMLFdBQU87QUFBQSxFQUNUO0FBQ0Y7OztBR3ZObVgsU0FBUyxjQUFBQyxhQUFZLGdCQUFBQyxxQkFBb0I7QUFDNVosU0FBUyxXQUFBQyxVQUFTLFlBQUFDLGlCQUFnQjtBQUNsQyxTQUFTLFlBQUFDLGlCQUFnQjtBQUd6QixJQUFNLGFBQWE7QUFFbkIsU0FBUyxlQUFlLFNBQVNDLGNBQWEsUUFBUTtBQUNwRCxRQUFNLGtCQUFrQkMsb0JBQW1CRCxZQUFXO0FBQ3RELE1BQUksQ0FBQyxpQkFBaUI7QUFDcEIsV0FBTyxNQUFNLDRCQUE0QjtBQUN6QyxXQUFPO0FBQUEsRUFDVDtBQUNBLFFBQU0sU0FBUyxnQkFBZ0IsUUFBUTtBQUN2QyxNQUFJLENBQUMsUUFBUTtBQUNYLFdBQU8sTUFBTSx1Q0FBdUM7QUFDcEQsV0FBTztBQUFBLEVBQ1Q7QUFFQSxXQUFTLFVBQVUsT0FBTyxLQUFLLE1BQU0sR0FBRztBQUN0QyxVQUFNLFlBQVksT0FBTyxNQUFNO0FBRS9CLGFBQVMsWUFBWSxPQUFPLEtBQUssU0FBUyxHQUFHO0FBRTNDLFVBQUksUUFBUSxXQUFXLFVBQVUsUUFBUSxDQUFDLEdBQUc7QUFDM0MsY0FBTSxhQUFhLFFBQVEsUUFBUSxVQUFVLFFBQVEsR0FBRyxFQUFFO0FBQzFELGNBQU0sUUFBUUUsVUFBU0MsU0FBUSxpQkFBaUIsUUFBUSxRQUFRLEdBQUcsRUFBRSxPQUFPLEtBQUssQ0FBQztBQUVsRixpQkFBUyxRQUFRLE9BQU87QUFDdEIsY0FBSSxLQUFLLFNBQVMsVUFBVTtBQUFHLG1CQUFPO0FBQUEsUUFDeEM7QUFBQSxNQUNGO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFDQSxTQUFPO0FBQ1Q7QUFFQSxTQUFTRixvQkFBbUJELGNBQWE7QUFDdkMsUUFBTSxvQkFBb0JHLFNBQVFILGNBQWEsWUFBWTtBQUMzRCxNQUFJLENBQUNJLFlBQVcsaUJBQWlCLEdBQUc7QUFDbEMsV0FBTyxDQUFDO0FBQUEsRUFDVjtBQUNBLFFBQU0sNEJBQTRCQyxjQUFhLGlCQUFpQjtBQUNoRSxNQUFJLDBCQUEwQixXQUFXLEdBQUc7QUFDMUMsV0FBTyxDQUFDO0FBQUEsRUFDVjtBQUNBLFNBQU8sS0FBSyxNQUFNLHlCQUF5QjtBQUM3QztBQUVBLFNBQVMsZUFBZSxRQUFRLHVCQUF1QkwsY0FBYSxRQUFRLFNBQVM7QUFDbkYsV0FBUyxPQUFPLFFBQVEsWUFBWSxTQUFVLE9BQU8sS0FBSyxXQUFXTSxVQUFTLFNBQVMsV0FBVztBQUNoRyxRQUFJLGVBQWVILFNBQVEsdUJBQXVCRyxVQUFTLE9BQU87QUFDbEUsVUFBTSx3QkFBd0IsYUFBYSxXQUFXTixZQUFXLEtBQUtJLFlBQVcsWUFBWTtBQUM3RixRQUFJLHlCQUF5QixlQUFlLFNBQVNKLGNBQWEsTUFBTSxHQUFHO0FBR3pFLFlBQU0sY0FBYyxRQUFRLFVBQVUsT0FBTztBQUU3QyxZQUFNLGFBQWEsd0JBQXdCLEtBQUs7QUFDaEQsWUFBTSxzQkFBc0IsYUFBYSxZQUFZTyxVQUFTUCxZQUFXO0FBQ3pFLGFBQU87QUFBQSxRQUNMO0FBQUEsUUFDQSxNQUFNTSxXQUFVLFVBQVU7QUFBQSxRQUMxQjtBQUFBLFFBQ0EsTUFBTSxzQkFBc0IsTUFBTSxVQUFVO0FBQUEsTUFDOUM7QUFDQSxZQUFNLGVBQWUsYUFBYSxVQUFVTixhQUFZLE1BQU0sRUFBRSxRQUFRLE9BQU8sR0FBRztBQUdsRixhQUFPLE9BQU8sYUFBYSxNQUFNLHNCQUFzQixlQUFlO0FBQUEsSUFDeEUsV0FBVyxRQUFRLFNBQVM7QUFDMUIsYUFBTyxJQUFJLG9CQUFvQixPQUFPLDhCQUE4QjtBQUFBLElBQ3RFLE9BQU87QUFFTCxhQUFPLE9BQU8sYUFBYSxNQUFNLFdBQVcsVUFBVTtBQUFBLElBQ3hEO0FBQ0EsV0FBTztBQUFBLEVBQ1QsQ0FBQztBQUNELFNBQU87QUFDVDs7O0FDL0VBO0FBQUEsRUFDRSxnQkFBa0I7QUFBQSxFQUNsQixhQUFlO0FBQUEsRUFDZixxQkFBdUI7QUFBQSxFQUN2QixjQUFnQjtBQUFBLEVBQ2hCLGlCQUFtQjtBQUFBLEVBQ25CLGFBQWU7QUFBQSxFQUNmLHNCQUF3QjtBQUFBLEVBQ3hCLGlCQUFtQjtBQUFBLEVBQ25CLHNCQUF3QjtBQUFBLEVBQ3hCLG9CQUFzQjtBQUFBLEVBQ3RCLFdBQWE7QUFBQSxFQUNiLDJCQUE2QjtBQUFBLEVBQzdCLFlBQWM7QUFBQSxFQUNkLGdCQUFrQjtBQUFBLEVBQ2xCLGFBQWU7QUFDakI7OztBTEZBO0FBQUEsRUFHRTtBQUFBLEVBQ0E7QUFBQSxPQUtLO0FBQ1AsU0FBUyxtQkFBbUI7QUFFNUIsWUFBWSxZQUFZO0FBQ3hCLE9BQU8sWUFBWTtBQUNuQixPQUFPLGFBQWE7QUFDcEIsT0FBTyxhQUFhOzs7QU1GcEIsU0FBUyxvQkFBb0I7QUFDN0IsT0FBTyxrQkFBa0I7QUFFekIsSUFBTSxhQUFhO0FBRW5CLElBQU0sU0FBUyxDQUFDLFFBQ2QsSUFDRyxRQUFRLFlBQVkseUNBQXlDLEVBQzdELFFBQVEsTUFBTSxLQUFLLEVBQ25CLFFBQVEsWUFBWSxNQUFNO0FBRWhCLFNBQVIsV0FBNEIsVUFBVSxDQUFDLEdBQUc7QUFDL0MsUUFBTSxpQkFBaUI7QUFBQSxJQUNyQixTQUFTO0FBQUEsSUFDVCxTQUFTO0FBQUEsSUFDVCxlQUFlO0FBQUEsRUFDakI7QUFFQSxRQUFNLE9BQU8sRUFBRSxHQUFHLGdCQUFnQixHQUFHLFFBQVE7QUFDN0MsUUFBTSxTQUFTLGFBQWEsS0FBSyxTQUFTLEtBQUssT0FBTztBQUV0RCxTQUFPO0FBQUEsSUFDTCxNQUFNO0FBQUEsSUFDTixTQUFTO0FBQUEsSUFDVCxVQUFVLE1BQU0sSUFBSTtBQUNsQixVQUFJLENBQUMsT0FBTyxFQUFFO0FBQUc7QUFDakIsWUFBTSxNQUFNLEtBQUssTUFBTSxNQUFNLENBQUMsQ0FBQztBQUUvQixVQUFJO0FBR0osVUFBSSx1QkFBdUI7QUFDM0IsWUFBTSxjQUFjLGFBQWEsTUFBTSxFQUFFLElBQVMsR0FBRyxDQUFDLFNBQVM7QUFDN0QsWUFBSSxLQUFLLFNBQVMsNEJBQTRCO0FBQzVDLDhCQUFvQixLQUFLLFlBQVk7QUFFckMsaUNBQXVCLEtBQUssWUFBWSxTQUFTO0FBQUEsUUFDbkQ7QUFBQSxNQUNGLENBQUM7QUFFRCxVQUFJLENBQUMscUJBQXFCLENBQUMsc0JBQXNCO0FBQy9DO0FBQUEsTUFDRjtBQUNBLGtCQUFZLEtBQUssQ0FBQyxTQUFTO0FBQ3pCLFlBQUkscUJBQXFCLEtBQUssU0FBUyx1QkFBdUI7QUFDNUQsZ0JBQU0sY0FBYyxLQUFLLGFBQWEsS0FBSyxDQUFDLE1BQU0sRUFBRSxHQUFHLFNBQVMsaUJBQWlCO0FBQ2pGLGNBQUksYUFBYTtBQUNmLHdCQUFZLEtBQUssS0FBSyxPQUFPLFdBQVcsT0FBTyxZQUFZLEtBQUssS0FBSyxDQUFDLElBQUk7QUFBQSxVQUM1RTtBQUFBLFFBQ0Y7QUFFQSxZQUFJLHdCQUF3QixLQUFLLFNBQVMsNEJBQTRCO0FBQ3BFLGVBQUssWUFBWSxLQUFLLE9BQU8sV0FBVyxPQUFPLEtBQUssWUFBWSxLQUFLLENBQUMsSUFBSTtBQUFBLFFBQzVFO0FBQUEsTUFDRixDQUFDO0FBQ0Qsa0JBQVksUUFBUSwyREFBMkQsS0FBSyxhQUFhO0FBQUEsQ0FBTTtBQUN2RyxhQUFPO0FBQUEsUUFDTCxNQUFNLFlBQVksU0FBUztBQUFBLFFBQzNCLEtBQUssWUFBWSxZQUFZO0FBQUEsVUFDM0IsT0FBTztBQUFBLFFBQ1QsQ0FBQztBQUFBLE1BQ0g7QUFBQSxJQUNGO0FBQUEsRUFDRjtBQUNGOzs7QU4zREEsU0FBUyxxQkFBcUI7QUFFOUIsU0FBUyxrQkFBa0I7QUFsQzNCLElBQU0sbUNBQW1DO0FBQTZILElBQU0sMkNBQTJDO0FBcUN2TixJQUFNUSxXQUFVLGNBQWMsd0NBQWU7QUFFN0MsSUFBTSxjQUFjO0FBRXBCLElBQU0saUJBQWlCLEtBQUssUUFBUSxrQ0FBVyxtQ0FBUyxjQUFjO0FBQ3RFLElBQU0sY0FBYyxLQUFLLFFBQVEsZ0JBQWdCLG1DQUFTLFdBQVc7QUFDckUsSUFBTSx1QkFBdUIsS0FBSyxRQUFRLGtDQUFXLG1DQUFTLG9CQUFvQjtBQUNsRixJQUFNLGtCQUFrQixLQUFLLFFBQVEsa0NBQVcsbUNBQVMsZUFBZTtBQUN4RSxJQUFNLFlBQVksQ0FBQyxDQUFDLFFBQVEsSUFBSTtBQUNoQyxJQUFNLHFCQUFxQixLQUFLLFFBQVEsa0NBQVcsbUNBQVMsa0JBQWtCO0FBQzlFLElBQU0sc0JBQXNCLEtBQUssUUFBUSxrQ0FBVyxtQ0FBUyxtQkFBbUI7QUFDaEYsSUFBTSx5QkFBeUIsS0FBSyxRQUFRLGtDQUFXLGNBQWM7QUFFckUsSUFBTSxvQkFBb0IsWUFBWSxrQkFBa0I7QUFDeEQsSUFBTSxjQUFjLEtBQUssUUFBUSxrQ0FBVyxZQUFZLG1DQUFTLHVCQUF1QixtQ0FBUyxXQUFXO0FBQzVHLElBQU0sWUFBWSxLQUFLLFFBQVEsYUFBYSxZQUFZO0FBQ3hELElBQU0saUJBQWlCLEtBQUssUUFBUSxhQUFhLGtCQUFrQjtBQUNuRSxJQUFNLG9CQUFvQixLQUFLLFFBQVEsa0NBQVcsY0FBYztBQUNoRSxJQUFNLG1CQUFtQjtBQUV6QixJQUFNLG1CQUFtQixLQUFLLFFBQVEsZ0JBQWdCLFlBQVk7QUFFbEUsSUFBTSw2QkFBNkI7QUFBQSxFQUNqQyxLQUFLLFFBQVEsa0NBQVcsT0FBTyxRQUFRLGFBQWEsWUFBWSxXQUFXO0FBQUEsRUFDM0UsS0FBSyxRQUFRLGtDQUFXLE9BQU8sUUFBUSxhQUFhLFFBQVE7QUFBQSxFQUM1RDtBQUNGO0FBR0EsSUFBTSxzQkFBc0IsMkJBQTJCLElBQUksQ0FBQyxXQUFXLEtBQUssUUFBUSxRQUFRLG1DQUFTLFdBQVcsQ0FBQztBQUVqSCxJQUFNLGVBQWU7QUFBQSxFQUNuQixTQUFTO0FBQUEsRUFDVCxjQUFjO0FBQUE7QUFBQTtBQUFBLEVBR2QscUJBQXFCLEtBQUssUUFBUSxxQkFBcUIsbUNBQVMsV0FBVztBQUFBLEVBQzNFO0FBQUEsRUFDQSxpQ0FBaUMsWUFDN0IsS0FBSyxRQUFRLGlCQUFpQixXQUFXLElBQ3pDLEtBQUssUUFBUSxrQ0FBVyxtQ0FBUyxZQUFZO0FBQUEsRUFDakQseUJBQXlCLEtBQUssUUFBUSxnQkFBZ0IsbUNBQVMsZUFBZTtBQUNoRjtBQUVBLElBQU0sMkJBQTJCQyxZQUFXLEtBQUssUUFBUSxnQkFBZ0Isb0JBQW9CLENBQUM7QUFHOUYsUUFBUSxRQUFRLE1BQU07QUFBQztBQUN2QixRQUFRLFFBQVEsTUFBTTtBQUFDO0FBRXZCLFNBQVMsMkJBQTBDO0FBQ2pELFFBQU0sOEJBQThCLENBQUMsYUFBYTtBQUNoRCxVQUFNLGFBQWEsU0FBUyxLQUFLLENBQUMsVUFBVSxNQUFNLFFBQVEsWUFBWTtBQUN0RSxRQUFJLFlBQVk7QUFDZCxpQkFBVyxNQUFNO0FBQUEsSUFDbkI7QUFFQSxXQUFPLEVBQUUsVUFBVSxVQUFVLENBQUMsRUFBRTtBQUFBLEVBQ2xDO0FBRUEsU0FBTztBQUFBLElBQ0wsTUFBTTtBQUFBLElBQ04sTUFBTSxVQUFVLE1BQU0sSUFBSTtBQUN4QixVQUFJLGVBQWUsS0FBSyxFQUFFLEdBQUc7QUFDM0IsY0FBTSxFQUFFLGdCQUFnQixJQUFJLE1BQU0sWUFBWTtBQUFBLFVBQzVDLGVBQWU7QUFBQSxVQUNmLGNBQWMsQ0FBQyxNQUFNO0FBQUEsVUFDckIsYUFBYSxDQUFDLFNBQVM7QUFBQSxVQUN2QixvQkFBb0IsQ0FBQywyQkFBMkI7QUFBQSxVQUNoRCwrQkFBK0IsTUFBTSxPQUFPO0FBQUE7QUFBQSxRQUM5QyxDQUFDO0FBRUQsZUFBTyxLQUFLLFFBQVEsc0JBQXNCLEtBQUssVUFBVSxlQUFlLENBQUM7QUFBQSxNQUMzRTtBQUFBLElBQ0Y7QUFBQSxFQUNGO0FBQ0Y7QUFFQSxTQUFTLGNBQWMsTUFBb0I7QUFDekMsTUFBSTtBQUNKLFFBQU0sVUFBVSxLQUFLO0FBRXJCLFFBQU0sUUFBUSxDQUFDO0FBRWYsaUJBQWUsTUFBTSxRQUE4QixvQkFBcUMsQ0FBQyxHQUFHO0FBQzFGLFVBQU0sc0JBQXNCO0FBQUEsTUFDMUI7QUFBQSxNQUNBO0FBQUEsTUFDQTtBQUFBLE1BQ0E7QUFBQSxJQUNGO0FBQ0EsVUFBTSxVQUEyQixPQUFPLFFBQVEsT0FBTyxDQUFDLE1BQU07QUFDNUQsYUFBTyxvQkFBb0IsU0FBUyxFQUFFLElBQUk7QUFBQSxJQUM1QyxDQUFDO0FBQ0QsVUFBTSxXQUFXLE9BQU8sZUFBZTtBQUN2QyxVQUFNLGdCQUErQjtBQUFBLE1BQ25DLE1BQU07QUFBQSxNQUNOLFVBQVUsUUFBUSxVQUFVLFVBQVU7QUFDcEMsZUFBTyxTQUFTLFFBQVEsUUFBUTtBQUFBLE1BQ2xDO0FBQUEsSUFDRjtBQUNBLFlBQVEsUUFBUSxhQUFhO0FBQzdCLFlBQVE7QUFBQSxNQUNOLFFBQVE7QUFBQSxRQUNOLFFBQVE7QUFBQSxVQUNOLHdCQUF3QixLQUFLLFVBQVUsT0FBTyxJQUFJO0FBQUEsVUFDbEQsR0FBRyxPQUFPO0FBQUEsUUFDWjtBQUFBLFFBQ0EsbUJBQW1CO0FBQUEsTUFDckIsQ0FBQztBQUFBLElBQ0g7QUFDQSxRQUFJLG1CQUFtQjtBQUNyQixjQUFRLEtBQUssR0FBRyxpQkFBaUI7QUFBQSxJQUNuQztBQUNBLFVBQU0sU0FBUyxNQUFhLGNBQU87QUFBQSxNQUNqQyxPQUFPLEtBQUssUUFBUSxtQ0FBUyx5QkFBeUI7QUFBQSxNQUN0RDtBQUFBLElBQ0YsQ0FBQztBQUVELFFBQUk7QUFDRixhQUFPLE1BQU0sT0FBTyxNQUFNLEVBQUU7QUFBQSxRQUMxQixNQUFNLEtBQUssUUFBUSxtQkFBbUIsT0FBTztBQUFBLFFBQzdDLFFBQVE7QUFBQSxRQUNSLFNBQVM7QUFBQSxRQUNULFdBQVcsT0FBTyxZQUFZLFdBQVcsT0FBTyxNQUFNO0FBQUEsUUFDdEQsc0JBQXNCO0FBQUEsTUFDeEIsQ0FBQztBQUFBLElBQ0gsVUFBRTtBQUNBLFlBQU0sT0FBTyxNQUFNO0FBQUEsSUFDckI7QUFBQSxFQUNGO0FBRUEsU0FBTztBQUFBLElBQ0wsTUFBTTtBQUFBLElBQ04sU0FBUztBQUFBLElBQ1QsTUFBTSxlQUFlLGdCQUFnQjtBQUNuQyxlQUFTO0FBQUEsSUFDWDtBQUFBLElBQ0EsTUFBTSxhQUFhO0FBQ2pCLFVBQUksU0FBUztBQUNYLGNBQU0sRUFBRSxPQUFPLElBQUksTUFBTSxNQUFNLFVBQVU7QUFDekMsY0FBTSxPQUFPLE9BQU8sQ0FBQyxFQUFFO0FBQ3ZCLGNBQU0sTUFBTSxPQUFPLENBQUMsRUFBRTtBQUFBLE1BQ3hCO0FBQUEsSUFDRjtBQUFBLElBQ0EsTUFBTSxLQUFLLElBQUk7QUFDYixVQUFJLEdBQUcsU0FBUyxPQUFPLEdBQUc7QUFDeEIsZUFBTztBQUFBLE1BQ1Q7QUFBQSxJQUNGO0FBQUEsSUFDQSxNQUFNLFVBQVUsT0FBTyxJQUFJO0FBQ3pCLFVBQUksR0FBRyxTQUFTLE9BQU8sR0FBRztBQUN4QixlQUFPO0FBQUEsTUFDVDtBQUFBLElBQ0Y7QUFBQSxJQUNBLE1BQU0sY0FBYztBQUNsQixVQUFJLENBQUMsU0FBUztBQUNaLGNBQU0sTUFBTSxTQUFTLENBQUMseUJBQXlCLEdBQUcsT0FBTyxDQUFDLENBQUM7QUFBQSxNQUM3RDtBQUFBLElBQ0Y7QUFBQSxFQUNGO0FBQ0Y7QUFFQSxTQUFTLHVCQUFxQztBQUM1QyxXQUFTLDRCQUE0QixtQkFBMkMsV0FBbUI7QUFDakcsVUFBTSxZQUFZLEtBQUssUUFBUSxnQkFBZ0IsbUNBQVMsYUFBYSxXQUFXLFlBQVk7QUFDNUYsUUFBSUEsWUFBVyxTQUFTLEdBQUc7QUFDekIsWUFBTSxtQkFBbUJDLGNBQWEsV0FBVyxFQUFFLFVBQVUsUUFBUSxDQUFDLEVBQUUsUUFBUSxTQUFTLElBQUk7QUFDN0Ysd0JBQWtCLFNBQVMsSUFBSTtBQUMvQixZQUFNLGtCQUFrQixLQUFLLE1BQU0sZ0JBQWdCO0FBQ25ELFVBQUksZ0JBQWdCLFFBQVE7QUFDMUIsb0NBQTRCLG1CQUFtQixnQkFBZ0IsTUFBTTtBQUFBLE1BQ3ZFO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFFQSxTQUFPO0FBQUEsSUFDTCxNQUFNO0FBQUEsSUFDTixTQUFTO0FBQUEsSUFDVCxNQUFNLFlBQVksU0FBd0IsUUFBdUQ7QUF4TnJHO0FBeU5NLFlBQU0sVUFBVSxPQUFPLE9BQU8sTUFBTSxFQUFFLFFBQVEsQ0FBQyxNQUFPLEVBQUUsVUFBVSxPQUFPLEtBQUssRUFBRSxPQUFPLElBQUksQ0FBQyxDQUFFO0FBQzlGLFlBQU0scUJBQXFCLFFBQ3hCLElBQUksQ0FBQyxPQUFPLEdBQUcsUUFBUSxPQUFPLEdBQUcsQ0FBQyxFQUNsQyxPQUFPLENBQUMsT0FBTyxHQUFHLFdBQVcsa0JBQWtCLFFBQVEsT0FBTyxHQUFHLENBQUMsQ0FBQyxFQUNuRSxJQUFJLENBQUMsT0FBTyxHQUFHLFVBQVUsa0JBQWtCLFNBQVMsQ0FBQyxDQUFDO0FBQ3pELFlBQU0sYUFBYSxtQkFDaEIsSUFBSSxDQUFDLE9BQU8sR0FBRyxRQUFRLE9BQU8sR0FBRyxDQUFDLEVBQ2xDLElBQUksQ0FBQyxPQUFPO0FBQ1gsY0FBTSxRQUFRLEdBQUcsTUFBTSxHQUFHO0FBQzFCLFlBQUksR0FBRyxXQUFXLEdBQUcsR0FBRztBQUN0QixpQkFBTyxNQUFNLENBQUMsSUFBSSxNQUFNLE1BQU0sQ0FBQztBQUFBLFFBQ2pDLE9BQU87QUFDTCxpQkFBTyxNQUFNLENBQUM7QUFBQSxRQUNoQjtBQUFBLE1BQ0YsQ0FBQyxFQUNBLEtBQUssRUFDTCxPQUFPLENBQUMsT0FBTyxPQUFPLFNBQVMsS0FBSyxRQUFRLEtBQUssTUFBTSxLQUFLO0FBQy9ELFlBQU0sc0JBQXNCLE9BQU8sWUFBWSxXQUFXLElBQUksQ0FBQyxXQUFXLENBQUMsUUFBUSxXQUFXLE1BQU0sQ0FBQyxDQUFDLENBQUM7QUFDdkcsWUFBTSxRQUFRLE9BQU87QUFBQSxRQUNuQixXQUNHLE9BQU8sQ0FBQyxXQUFXLFlBQVksTUFBTSxLQUFLLElBQUksRUFDOUMsSUFBSSxDQUFDLFdBQVcsQ0FBQyxRQUFRLEVBQUUsTUFBTSxZQUFZLE1BQU0sR0FBRyxTQUFTLFdBQVcsTUFBTSxFQUFFLENBQUMsQ0FBQztBQUFBLE1BQ3pGO0FBRUEsTUFBQUMsV0FBVSxLQUFLLFFBQVEsU0FBUyxHQUFHLEVBQUUsV0FBVyxLQUFLLENBQUM7QUFDdEQsWUFBTSxxQkFBcUIsS0FBSyxNQUFNRCxjQUFhLHdCQUF3QixFQUFFLFVBQVUsUUFBUSxDQUFDLENBQUM7QUFFakcsWUFBTSxlQUFlLE9BQU8sT0FBTyxNQUFNLEVBQ3RDLE9BQU8sQ0FBQ0UsWUFBV0EsUUFBTyxPQUFPLEVBQ2pDLElBQUksQ0FBQ0EsWUFBV0EsUUFBTyxRQUFRO0FBRWxDLFlBQU0scUJBQXFCLEtBQUssUUFBUSxtQkFBbUIsWUFBWTtBQUN2RSxZQUFNLGtCQUEwQkYsY0FBYSxrQkFBa0IsRUFBRSxVQUFVLFFBQVEsQ0FBQztBQUNwRixZQUFNLHFCQUE2QkEsY0FBYSxvQkFBb0I7QUFBQSxRQUNsRSxVQUFVO0FBQUEsTUFDWixDQUFDO0FBRUQsWUFBTSxrQkFBa0IsSUFBSSxJQUFJLGdCQUFnQixNQUFNLFFBQVEsRUFBRSxPQUFPLENBQUMsUUFBUSxJQUFJLEtBQUssTUFBTSxFQUFFLENBQUM7QUFDbEcsWUFBTSxxQkFBcUIsbUJBQW1CLE1BQU0sUUFBUSxFQUFFLE9BQU8sQ0FBQyxRQUFRLElBQUksS0FBSyxNQUFNLEVBQUU7QUFFL0YsWUFBTSxnQkFBMEIsQ0FBQztBQUNqQyx5QkFBbUIsUUFBUSxDQUFDLFFBQVE7QUFDbEMsWUFBSSxDQUFDLGdCQUFnQixJQUFJLEdBQUcsR0FBRztBQUM3Qix3QkFBYyxLQUFLLEdBQUc7QUFBQSxRQUN4QjtBQUFBLE1BQ0YsQ0FBQztBQUlELFlBQU0sZUFBZSxDQUFDLFVBQWtCLFdBQThCO0FBQ3BFLGNBQU0sVUFBa0JBLGNBQWEsVUFBVSxFQUFFLFVBQVUsUUFBUSxDQUFDO0FBQ3BFLGNBQU0sUUFBUSxRQUFRLE1BQU0sSUFBSTtBQUNoQyxjQUFNLGdCQUFnQixNQUNuQixPQUFPLENBQUMsU0FBUyxLQUFLLFdBQVcsU0FBUyxDQUFDLEVBQzNDLElBQUksQ0FBQyxTQUFTLEtBQUssVUFBVSxLQUFLLFFBQVEsR0FBRyxJQUFJLEdBQUcsS0FBSyxZQUFZLEdBQUcsQ0FBQyxDQUFDLEVBQzFFLElBQUksQ0FBQyxTQUFVLEtBQUssU0FBUyxHQUFHLElBQUksS0FBSyxVQUFVLEdBQUcsS0FBSyxZQUFZLEdBQUcsQ0FBQyxJQUFJLElBQUs7QUFDdkYsY0FBTSxpQkFBaUIsTUFDcEIsT0FBTyxDQUFDLFNBQVMsS0FBSyxTQUFTLFNBQVMsQ0FBQyxFQUN6QyxJQUFJLENBQUMsU0FBUyxLQUFLLFFBQVEsY0FBYyxFQUFFLENBQUMsRUFDNUMsSUFBSSxDQUFDLFNBQVMsS0FBSyxNQUFNLEdBQUcsRUFBRSxDQUFDLENBQUMsRUFDaEMsSUFBSSxDQUFDLFNBQVUsS0FBSyxTQUFTLEdBQUcsSUFBSSxLQUFLLFVBQVUsR0FBRyxLQUFLLFlBQVksR0FBRyxDQUFDLElBQUksSUFBSztBQUV2RixzQkFBYyxRQUFRLENBQUMsaUJBQWlCLE9BQU8sSUFBSSxZQUFZLENBQUM7QUFFaEUsdUJBQWUsSUFBSSxDQUFDLGtCQUFrQjtBQUNwQyxnQkFBTSxlQUFlLEtBQUssUUFBUSxLQUFLLFFBQVEsUUFBUSxHQUFHLGFBQWE7QUFDdkUsdUJBQWEsY0FBYyxNQUFNO0FBQUEsUUFDbkMsQ0FBQztBQUFBLE1BQ0g7QUFFQSxZQUFNLHNCQUFzQixvQkFBSSxJQUFZO0FBQzVDO0FBQUEsUUFDRSxLQUFLLFFBQVEsYUFBYSx5QkFBeUIsUUFBUSwyQkFBMkI7QUFBQSxRQUN0RjtBQUFBLE1BQ0Y7QUFDQSxZQUFNLG1CQUFtQixNQUFNLEtBQUssbUJBQW1CLEVBQUUsS0FBSztBQUU5RCxZQUFNLGdCQUF3QyxDQUFDO0FBRS9DLFlBQU0sd0JBQXdCLENBQUMsT0FBTyxXQUFXLE9BQU8sV0FBVyxRQUFRLFlBQVksUUFBUSxVQUFVO0FBRXpHLFlBQU0sNEJBQTRCLENBQUMsT0FDL0IsR0FBRyxXQUFXLGFBQWEsd0JBQXdCLFFBQVEsT0FBTyxHQUFHLENBQUMsS0FDL0QsR0FBRyxNQUFNLGlEQUFpRDtBQUtyRSxjQUNHLElBQUksQ0FBQyxPQUFPLEdBQUcsUUFBUSxPQUFPLEdBQUcsQ0FBQyxFQUNsQyxPQUFPLENBQUMsT0FBTyxHQUFHLFdBQVcsZUFBZSxRQUFRLE9BQU8sR0FBRyxDQUFDLENBQUMsRUFDaEUsT0FBTyxDQUFDLE9BQU8sQ0FBQyxHQUFHLFdBQVcsYUFBYSx3QkFBd0IsUUFBUSxPQUFPLEdBQUcsQ0FBQyxLQUFLLDBCQUEwQixFQUFFLENBQUMsRUFDeEgsSUFBSSxDQUFDLE9BQU8sR0FBRyxVQUFVLGVBQWUsU0FBUyxDQUFDLENBQUMsRUFDbkQsSUFBSSxDQUFDLFNBQWtCLEtBQUssU0FBUyxHQUFHLElBQUksS0FBSyxVQUFVLEdBQUcsS0FBSyxZQUFZLEdBQUcsQ0FBQyxJQUFJLElBQUssRUFDNUYsUUFBUSxDQUFDLFNBQWlCO0FBRXpCLGNBQU0sV0FBVyxLQUFLLFFBQVEsZ0JBQWdCLElBQUk7QUFDbEQsWUFBSSxzQkFBc0IsU0FBUyxLQUFLLFFBQVEsUUFBUSxDQUFDLEdBQUc7QUFDMUQsZ0JBQU0sYUFBYUEsY0FBYSxVQUFVLEVBQUUsVUFBVSxRQUFRLENBQUMsRUFBRSxRQUFRLFNBQVMsSUFBSTtBQUN0Rix3QkFBYyxJQUFJLElBQUksV0FBVyxRQUFRLEVBQUUsT0FBTyxZQUFZLE1BQU0sRUFBRSxPQUFPLEtBQUs7QUFBQSxRQUNwRjtBQUFBLE1BQ0YsQ0FBQztBQUdILHVCQUNHLE9BQU8sQ0FBQyxTQUFpQixLQUFLLFNBQVMseUJBQXlCLENBQUMsRUFDakUsUUFBUSxDQUFDLFNBQWlCO0FBQ3pCLFlBQUksV0FBVyxLQUFLLFVBQVUsS0FBSyxRQUFRLFdBQVcsQ0FBQztBQUV2RCxjQUFNLGFBQWFBLGNBQWEsS0FBSyxRQUFRLGdCQUFnQixRQUFRLEdBQUcsRUFBRSxVQUFVLFFBQVEsQ0FBQyxFQUFFO0FBQUEsVUFDN0Y7QUFBQSxVQUNBO0FBQUEsUUFDRjtBQUNBLGNBQU0sT0FBTyxXQUFXLFFBQVEsRUFBRSxPQUFPLFlBQVksTUFBTSxFQUFFLE9BQU8sS0FBSztBQUV6RSxjQUFNLFVBQVUsS0FBSyxVQUFVLEtBQUssUUFBUSxnQkFBZ0IsSUFBSSxFQUFFO0FBQ2xFLHNCQUFjLE9BQU8sSUFBSTtBQUFBLE1BQzNCLENBQUM7QUFFSCxVQUFJRCxZQUFXLEtBQUssUUFBUSxnQkFBZ0IsVUFBVSxDQUFDLEdBQUc7QUFDeEQsY0FBTSxhQUFhQyxjQUFhLEtBQUssUUFBUSxnQkFBZ0IsVUFBVSxHQUFHLEVBQUUsVUFBVSxRQUFRLENBQUMsRUFBRTtBQUFBLFVBQy9GO0FBQUEsVUFDQTtBQUFBLFFBQ0Y7QUFDQSxzQkFBYyxVQUFVLElBQUksV0FBVyxRQUFRLEVBQUUsT0FBTyxZQUFZLE1BQU0sRUFBRSxPQUFPLEtBQUs7QUFBQSxNQUMxRjtBQUVBLFlBQU0sb0JBQTRDLENBQUM7QUFDbkQsWUFBTSxlQUFlLEtBQUssUUFBUSxvQkFBb0IsUUFBUTtBQUM5RCxVQUFJRCxZQUFXLFlBQVksR0FBRztBQUM1QixRQUFBSSxhQUFZLFlBQVksRUFBRSxRQUFRLENBQUNDLGlCQUFnQjtBQUNqRCxnQkFBTSxZQUFZLEtBQUssUUFBUSxjQUFjQSxjQUFhLFlBQVk7QUFDdEUsY0FBSUwsWUFBVyxTQUFTLEdBQUc7QUFDekIsOEJBQWtCLEtBQUssU0FBU0ssWUFBVyxDQUFDLElBQUlKLGNBQWEsV0FBVyxFQUFFLFVBQVUsUUFBUSxDQUFDLEVBQUU7QUFBQSxjQUM3RjtBQUFBLGNBQ0E7QUFBQSxZQUNGO0FBQUEsVUFDRjtBQUFBLFFBQ0YsQ0FBQztBQUFBLE1BQ0g7QUFFQSxrQ0FBNEIsbUJBQW1CLG1DQUFTLFNBQVM7QUFFakUsVUFBSSxnQkFBMEIsQ0FBQztBQUMvQixVQUFJLGtCQUFrQjtBQUNwQix3QkFBZ0IsaUJBQWlCLE1BQU0sR0FBRztBQUFBLE1BQzVDO0FBRUEsWUFBTSxRQUFRO0FBQUEsUUFDWix5QkFBeUIsbUJBQW1CO0FBQUEsUUFDNUMsWUFBWTtBQUFBLFFBQ1osZUFBZTtBQUFBLFFBQ2YsZ0JBQWdCO0FBQUEsUUFDaEI7QUFBQSxRQUNBO0FBQUEsUUFDQTtBQUFBLFFBQ0EsYUFBYTtBQUFBLFFBQ2Isa0JBQWlCLDhEQUFvQixXQUFwQixtQkFBNEI7QUFBQSxRQUM3QyxvQkFBb0I7QUFBQSxNQUN0QjtBQUNBLE1BQUFLLGVBQWMsV0FBVyxLQUFLLFVBQVUsT0FBTyxNQUFNLENBQUMsQ0FBQztBQUFBLElBQ3pEO0FBQUEsRUFDRjtBQUNGO0FBQ0EsU0FBUyxzQkFBb0M7QUFxQjNDLFFBQU0sa0JBQWtCO0FBRXhCLFFBQU0sbUJBQW1CLGtCQUFrQixRQUFRLE9BQU8sR0FBRztBQUU3RCxNQUFJO0FBRUosV0FBUyxjQUFjLElBQXlEO0FBQzlFLFVBQU0sQ0FBQyxPQUFPLGlCQUFpQixJQUFJLEdBQUcsTUFBTSxLQUFLLENBQUM7QUFDbEQsVUFBTSxjQUFjLE1BQU0sV0FBVyxHQUFHLElBQUksR0FBRyxLQUFLLElBQUksaUJBQWlCLEtBQUs7QUFDOUUsVUFBTSxhQUFhLElBQUksR0FBRyxVQUFVLFlBQVksTUFBTSxDQUFDO0FBQ3ZELFdBQU87QUFBQSxNQUNMO0FBQUEsTUFDQTtBQUFBLElBQ0Y7QUFBQSxFQUNGO0FBRUEsV0FBUyxXQUFXLElBQWtDO0FBQ3BELFVBQU0sRUFBRSxhQUFhLFdBQVcsSUFBSSxjQUFjLEVBQUU7QUFDcEQsVUFBTSxjQUFjLGlCQUFpQixTQUFTLFdBQVc7QUFFekQsUUFBSSxDQUFDO0FBQWE7QUFFbEIsVUFBTSxhQUF5QixZQUFZLFFBQVEsVUFBVTtBQUM3RCxRQUFJLENBQUM7QUFBWTtBQUVqQixVQUFNLGFBQWEsb0JBQUksSUFBWTtBQUNuQyxlQUFXLEtBQUssV0FBVyxTQUFTO0FBQ2xDLFVBQUksT0FBTyxNQUFNLFVBQVU7QUFDekIsbUJBQVcsSUFBSSxDQUFDO0FBQUEsTUFDbEIsT0FBTztBQUNMLGNBQU0sRUFBRSxXQUFXLE9BQU8sSUFBSTtBQUM5QixZQUFJLFdBQVc7QUFDYixxQkFBVyxJQUFJLFNBQVM7QUFBQSxRQUMxQixPQUFPO0FBQ0wsZ0JBQU0sZ0JBQWdCLFdBQVcsTUFBTTtBQUN2QyxjQUFJLGVBQWU7QUFDakIsMEJBQWMsUUFBUSxDQUFDQyxPQUFNLFdBQVcsSUFBSUEsRUFBQyxDQUFDO0FBQUEsVUFDaEQ7QUFBQSxRQUNGO0FBQUEsTUFDRjtBQUFBLElBQ0Y7QUFDQSxXQUFPLE1BQU0sS0FBSyxVQUFVO0FBQUEsRUFDOUI7QUFFQSxXQUFTLGlCQUFpQixTQUFpQjtBQUN6QyxXQUFPLFlBQVksWUFBWSx3QkFBd0I7QUFBQSxFQUN6RDtBQUVBLFdBQVMsbUJBQW1CLFNBQWlCO0FBQzNDLFdBQU8sWUFBWSxZQUFZLHNCQUFzQjtBQUFBLEVBQ3ZEO0FBRUEsU0FBTztBQUFBLElBQ0wsTUFBTTtBQUFBLElBQ04sU0FBUztBQUFBLElBQ1QsTUFBTSxRQUFRLEVBQUUsUUFBUSxHQUFHO0FBQ3pCLFVBQUksWUFBWTtBQUFTLGVBQU87QUFFaEMsVUFBSTtBQUNGLGNBQU0sdUJBQXVCUixTQUFRLFFBQVEsb0NBQW9DO0FBQ2pGLDJCQUFtQixLQUFLLE1BQU1FLGNBQWEsc0JBQXNCLEVBQUUsVUFBVSxPQUFPLENBQUMsQ0FBQztBQUFBLE1BQ3hGLFNBQVMsR0FBWTtBQUNuQixZQUFJLE9BQU8sTUFBTSxZQUFhLEVBQXVCLFNBQVMsb0JBQW9CO0FBQ2hGLDZCQUFtQixFQUFFLFVBQVUsQ0FBQyxFQUFFO0FBQ2xDLGtCQUFRLEtBQUssNkNBQTZDLGVBQWUsRUFBRTtBQUMzRSxpQkFBTztBQUFBLFFBQ1QsT0FBTztBQUNMLGdCQUFNO0FBQUEsUUFDUjtBQUFBLE1BQ0Y7QUFFQSxZQUFNLG9CQUErRixDQUFDO0FBQ3RHLGlCQUFXLENBQUMsTUFBTSxXQUFXLEtBQUssT0FBTyxRQUFRLGlCQUFpQixRQUFRLEdBQUc7QUFDM0UsWUFBSSxtQkFBdUM7QUFDM0MsWUFBSTtBQUNGLGdCQUFNLEVBQUUsU0FBUyxlQUFlLElBQUk7QUFDcEMsZ0JBQU0sMkJBQTJCLEtBQUssUUFBUSxrQkFBa0IsTUFBTSxjQUFjO0FBQ3BGLGdCQUFNLGNBQWMsS0FBSyxNQUFNQSxjQUFhLDBCQUEwQixFQUFFLFVBQVUsT0FBTyxDQUFDLENBQUM7QUFDM0YsNkJBQW1CLFlBQVk7QUFDL0IsY0FBSSxvQkFBb0IscUJBQXFCLGdCQUFnQjtBQUMzRCw4QkFBa0IsS0FBSztBQUFBLGNBQ3JCO0FBQUEsY0FDQTtBQUFBLGNBQ0E7QUFBQSxZQUNGLENBQUM7QUFBQSxVQUNIO0FBQUEsUUFDRixTQUFTLEdBQUc7QUFBQSxRQUVaO0FBQUEsTUFDRjtBQUNBLFVBQUksa0JBQWtCLFFBQVE7QUFDNUIsZ0JBQVEsS0FBSyxtRUFBbUUsZUFBZSxFQUFFO0FBQ2pHLGdCQUFRLEtBQUsscUNBQXFDLEtBQUssVUFBVSxtQkFBbUIsUUFBVyxDQUFDLENBQUMsRUFBRTtBQUNuRywyQkFBbUIsRUFBRSxVQUFVLENBQUMsRUFBRTtBQUNsQyxlQUFPO0FBQUEsTUFDVDtBQUVBLGFBQU87QUFBQSxJQUNUO0FBQUEsSUFDQSxNQUFNLE9BQU8sUUFBUTtBQUNuQixhQUFPO0FBQUEsUUFDTDtBQUFBLFVBQ0UsY0FBYztBQUFBLFlBQ1osU0FBUztBQUFBO0FBQUEsY0FFUDtBQUFBLGNBQ0EsR0FBRyxPQUFPLEtBQUssaUJBQWlCLFFBQVE7QUFBQSxjQUN4QztBQUFBLFlBQ0Y7QUFBQSxVQUNGO0FBQUEsUUFDRjtBQUFBLFFBQ0E7QUFBQSxNQUNGO0FBQUEsSUFDRjtBQUFBLElBQ0EsS0FBSyxPQUFPO0FBQ1YsWUFBTSxDQUFDTyxPQUFNLE1BQU0sSUFBSSxNQUFNLE1BQU0sR0FBRztBQUN0QyxVQUFJLENBQUNBLE1BQUssV0FBVyxnQkFBZ0I7QUFBRztBQUV4QyxZQUFNLEtBQUtBLE1BQUssVUFBVSxpQkFBaUIsU0FBUyxDQUFDO0FBQ3JELFlBQU0sV0FBVyxXQUFXLEVBQUU7QUFDOUIsVUFBSSxhQUFhO0FBQVc7QUFFNUIsWUFBTSxjQUFjLFNBQVMsSUFBSSxNQUFNLEtBQUs7QUFDNUMsWUFBTSxhQUFhLDRCQUE0QixXQUFXO0FBRTFELGFBQU8scUVBQXFFLFVBQVU7QUFBQTtBQUFBLFVBRWxGLFNBQVMsSUFBSSxrQkFBa0IsRUFBRSxLQUFLLElBQUksQ0FBQywrQ0FBK0MsRUFBRTtBQUFBLFdBQzNGLFNBQVMsSUFBSSxnQkFBZ0IsRUFBRSxLQUFLLElBQUksQ0FBQztBQUFBLElBQ2hEO0FBQUEsRUFDRjtBQUNGO0FBRUEsU0FBUyxZQUFZLE1BQW9CO0FBQ3ZDLFFBQU0sbUJBQW1CLEVBQUUsR0FBRyxjQUFjLFNBQVMsS0FBSyxRQUFRO0FBQ2xFLFNBQU87QUFBQSxJQUNMLE1BQU07QUFBQSxJQUNOLFNBQVM7QUFDUCw0QkFBc0Isa0JBQWtCLE9BQU87QUFBQSxJQUNqRDtBQUFBLElBQ0EsZ0JBQWdCLFFBQVE7QUFDdEIsZUFBUyw0QkFBNEIsV0FBVyxPQUFPO0FBQ3JELFlBQUksVUFBVSxXQUFXLFdBQVcsR0FBRztBQUNyQyxnQkFBTSxVQUFVLEtBQUssU0FBUyxhQUFhLFNBQVM7QUFDcEQsa0JBQVEsTUFBTSxpQkFBaUIsQ0FBQyxDQUFDLFFBQVEsWUFBWSxZQUFZLE9BQU87QUFDeEUsZ0NBQXNCLGtCQUFrQixPQUFPO0FBQUEsUUFDakQ7QUFBQSxNQUNGO0FBQ0EsYUFBTyxRQUFRLEdBQUcsT0FBTywyQkFBMkI7QUFDcEQsYUFBTyxRQUFRLEdBQUcsVUFBVSwyQkFBMkI7QUFBQSxJQUN6RDtBQUFBLElBQ0EsZ0JBQWdCLFNBQVM7QUFDdkIsWUFBTSxjQUFjLEtBQUssUUFBUSxRQUFRLElBQUk7QUFDN0MsWUFBTSxZQUFZLEtBQUssUUFBUSxXQUFXO0FBQzFDLFVBQUksWUFBWSxXQUFXLFNBQVMsR0FBRztBQUNyQyxjQUFNLFVBQVUsS0FBSyxTQUFTLFdBQVcsV0FBVztBQUVwRCxnQkFBUSxNQUFNLHNCQUFzQixPQUFPO0FBRTNDLFlBQUksUUFBUSxXQUFXLG1DQUFTLFNBQVMsR0FBRztBQUMxQyxnQ0FBc0Isa0JBQWtCLE9BQU87QUFBQSxRQUNqRDtBQUFBLE1BQ0Y7QUFBQSxJQUNGO0FBQUEsSUFDQSxNQUFNLFVBQVUsSUFBSSxVQUFVO0FBSTVCLFVBQ0UsS0FBSyxRQUFRLGFBQWEseUJBQXlCLFVBQVUsTUFBTSxZQUNuRSxDQUFDUixZQUFXLEtBQUssUUFBUSxhQUFhLHlCQUF5QixFQUFFLENBQUMsR0FDbEU7QUFDQSxnQkFBUSxNQUFNLHlCQUF5QixLQUFLLDBDQUEwQztBQUN0Riw4QkFBc0Isa0JBQWtCLE9BQU87QUFDL0M7QUFBQSxNQUNGO0FBQ0EsVUFBSSxDQUFDLEdBQUcsV0FBVyxtQ0FBUyxXQUFXLEdBQUc7QUFDeEM7QUFBQSxNQUNGO0FBRUEsaUJBQVcsWUFBWSxDQUFDLHFCQUFxQixjQUFjLEdBQUc7QUFDNUQsY0FBTSxTQUFTLE1BQU0sS0FBSyxRQUFRLEtBQUssUUFBUSxVQUFVLEVBQUUsQ0FBQztBQUM1RCxZQUFJLFFBQVE7QUFDVixpQkFBTztBQUFBLFFBQ1Q7QUFBQSxNQUNGO0FBQUEsSUFDRjtBQUFBLElBQ0EsTUFBTSxVQUFVLEtBQUssSUFBSSxTQUFTO0FBRWhDLFlBQU0sQ0FBQyxRQUFRLEtBQUssSUFBSSxHQUFHLE1BQU0sR0FBRztBQUNwQyxVQUNHLEVBQUMsaUNBQVEsV0FBVyxpQkFBZ0IsRUFBQyxpQ0FBUSxXQUFXLGFBQWEseUJBQ3RFLEVBQUMsaUNBQVEsU0FBUyxVQUNsQjtBQUNBO0FBQUEsTUFDRjtBQUNBLFlBQU0sQ0FBQyxTQUFTLElBQUksT0FBTyxVQUFVLFlBQVksU0FBUyxDQUFDLEVBQUUsTUFBTSxHQUFHO0FBQ3RFLGFBQU8sZUFBZSxLQUFLLEtBQUssUUFBUSxNQUFNLEdBQUcsS0FBSyxRQUFRLGFBQWEsU0FBUyxHQUFHLFNBQVMsSUFBSTtBQUFBLElBQ3RHO0FBQUEsRUFDRjtBQUNGO0FBRUEsU0FBUyxZQUFZLGNBQWMsY0FBYztBQUMvQyxRQUFNLFNBQWEsV0FBTztBQUMxQixTQUFPLFlBQVksTUFBTTtBQUN6QixTQUFPLEdBQUcsU0FBUyxTQUFVLEtBQUs7QUFDaEMsWUFBUSxJQUFJLDBEQUEwRCxHQUFHO0FBQ3pFLFdBQU8sUUFBUTtBQUNmLFlBQVEsS0FBSyxDQUFDO0FBQUEsRUFDaEIsQ0FBQztBQUNELFNBQU8sR0FBRyxTQUFTLFdBQVk7QUFDN0IsV0FBTyxRQUFRO0FBQ2YsZ0JBQVksY0FBYyxZQUFZO0FBQUEsRUFDeEMsQ0FBQztBQUVELFNBQU8sUUFBUSxjQUFjLGdCQUFnQixXQUFXO0FBQzFEO0FBRUEsSUFBSSw0QkFBNEI7QUFFaEMsSUFBTSx5QkFBeUIsQ0FBQyxnQkFBZ0IsaUJBQWlCO0FBRWpFLFNBQVMsc0JBQW9DO0FBQzNDLFNBQU87QUFBQSxJQUNMLE1BQU07QUFBQSxJQUNOLGdCQUFnQixTQUFTO0FBQ3ZCLGNBQVEsSUFBSSx1QkFBdUIsUUFBUSxNQUFNLFNBQVM7QUFBQSxJQUM1RDtBQUFBLEVBQ0Y7QUFDRjtBQUVBLElBQU0sd0JBQXdCO0FBQzlCLElBQU0sdUJBQXVCO0FBRTdCLFNBQVMscUJBQXFCO0FBQzVCLFNBQU87QUFBQSxJQUNMLE1BQU07QUFBQSxJQUVOLFVBQVUsS0FBYSxJQUFZO0FBQ2pDLFVBQUksR0FBRyxTQUFTLHlCQUF5QixHQUFHO0FBQzFDLFlBQUksSUFBSSxTQUFTLHVCQUF1QixHQUFHO0FBQ3pDLGdCQUFNLFNBQVMsSUFBSSxRQUFRLHVCQUF1QiwyQkFBMkI7QUFDN0UsY0FBSSxXQUFXLEtBQUs7QUFDbEIsb0JBQVEsTUFBTSwrQ0FBK0M7QUFBQSxVQUMvRCxXQUFXLENBQUMsT0FBTyxNQUFNLG9CQUFvQixHQUFHO0FBQzlDLG9CQUFRLE1BQU0sNENBQTRDO0FBQUEsVUFDNUQsT0FBTztBQUNMLG1CQUFPLEVBQUUsTUFBTSxPQUFPO0FBQUEsVUFDeEI7QUFBQSxRQUNGO0FBQUEsTUFDRjtBQUVBLGFBQU8sRUFBRSxNQUFNLElBQUk7QUFBQSxJQUNyQjtBQUFBLEVBQ0Y7QUFDRjtBQUVPLElBQU0sZUFBNkIsQ0FBQyxRQUFRO0FBQ2pELFFBQU0sVUFBVSxJQUFJLFNBQVM7QUFDN0IsUUFBTSxpQkFBaUIsQ0FBQyxXQUFXLENBQUM7QUFFcEMsTUFBSSxXQUFXLFFBQVEsSUFBSSxjQUFjO0FBR3ZDLGdCQUFZLFFBQVEsSUFBSSxjQUFjLFFBQVEsSUFBSSxZQUFZO0FBQUEsRUFDaEU7QUFFQSxTQUFPO0FBQUEsSUFDTCxNQUFNO0FBQUEsSUFDTixNQUFNO0FBQUEsSUFDTixXQUFXO0FBQUEsSUFDWCxTQUFTO0FBQUEsTUFDUCxPQUFPO0FBQUEsUUFDTCx5QkFBeUI7QUFBQSxRQUN6QixVQUFVO0FBQUEsTUFDWjtBQUFBLE1BQ0Esa0JBQWtCO0FBQUEsSUFDcEI7QUFBQSxJQUNBLFFBQVE7QUFBQSxNQUNOLGNBQWMsbUNBQVM7QUFBQSxNQUN2QixjQUFjO0FBQUEsSUFDaEI7QUFBQSxJQUNBLFFBQVE7QUFBQSxNQUNOLE1BQU07QUFBQSxNQUNOLFlBQVk7QUFBQSxNQUNaLElBQUk7QUFBQSxRQUNGLE9BQU87QUFBQSxNQUNUO0FBQUEsSUFDRjtBQUFBLElBQ0EsT0FBTztBQUFBLE1BQ0wsUUFBUTtBQUFBLE1BQ1IsYUFBYTtBQUFBLE1BQ2IsV0FBVztBQUFBLE1BQ1gsZUFBZTtBQUFBLFFBQ2IsT0FBTztBQUFBLFVBQ0wsV0FBVztBQUFBLFVBRVgsR0FBSSwyQkFBMkIsRUFBRSxrQkFBa0IsS0FBSyxRQUFRLGdCQUFnQixvQkFBb0IsRUFBRSxJQUFJLENBQUM7QUFBQSxRQUM3RztBQUFBLFFBQ0EsUUFBUSxDQUFDLFNBQStCLG1CQUEwQztBQUNoRixnQkFBTSxvQkFBb0I7QUFBQSxZQUN4QjtBQUFBLFlBQ0E7QUFBQSxZQUNBO0FBQUEsVUFDRjtBQUNBLGNBQUksUUFBUSxTQUFTLFVBQVUsUUFBUSxNQUFNLENBQUMsQ0FBQyxrQkFBa0IsS0FBSyxDQUFDLE9BQU8sUUFBUSxHQUFHLFNBQVMsRUFBRSxDQUFDLEdBQUc7QUFDdEc7QUFBQSxVQUNGO0FBQ0EseUJBQWUsT0FBTztBQUFBLFFBQ3hCO0FBQUEsTUFDRjtBQUFBLElBQ0Y7QUFBQSxJQUNBLGNBQWM7QUFBQSxNQUNaLFNBQVM7QUFBQTtBQUFBLFFBRVA7QUFBQSxNQUNGO0FBQUEsTUFDQSxTQUFTO0FBQUEsUUFDUDtBQUFBLFFBQ0E7QUFBQSxRQUNBO0FBQUEsUUFDQTtBQUFBLFFBQ0E7QUFBQSxRQUNBO0FBQUEsUUFDQTtBQUFBLE1BQ0Y7QUFBQSxJQUNGO0FBQUEsSUFDQSxTQUFTO0FBQUEsTUFDUCxrQkFBa0IsT0FBTztBQUFBLE1BQ3pCLFdBQVcsb0JBQW9CO0FBQUEsTUFDL0IsV0FBVyxvQkFBb0I7QUFBQSxNQUMvQixtQ0FBUyxrQkFBa0IsY0FBYyxFQUFFLFFBQVEsQ0FBQztBQUFBLE1BQ3BELENBQUMsV0FBVyxxQkFBcUI7QUFBQSxNQUNqQyxhQUFhLG1CQUFtQjtBQUFBLE1BQ2hDLFlBQVksRUFBRSxRQUFRLENBQUM7QUFBQSxNQUN2QixXQUFXO0FBQUEsUUFDVCxTQUFTLENBQUMsWUFBWSxpQkFBaUI7QUFBQSxRQUN2QyxTQUFTO0FBQUEsVUFDUCxHQUFHLFdBQVc7QUFBQSxVQUNkLElBQUksT0FBTyxHQUFHLFdBQVcsbUJBQW1CO0FBQUEsVUFDNUMsR0FBRyxtQkFBbUI7QUFBQSxVQUN0QixJQUFJLE9BQU8sR0FBRyxtQkFBbUIsbUJBQW1CO0FBQUEsVUFDcEQsSUFBSSxPQUFPLHNCQUFzQjtBQUFBLFFBQ25DO0FBQUEsTUFDRixDQUFDO0FBQUEsTUFDRDtBQUFBLFFBQ0UsTUFBTTtBQUFBLFFBQ04sb0JBQW9CO0FBQUEsVUFDbEIsU0FBUztBQUFBLFVBQ1QsVUFBVSxPQUFPLEVBQUUsT0FBTyxHQUFHO0FBQzNCLGdCQUFJLFVBQVUsQ0FBQywyQkFBMkI7QUFDeEMscUJBQU8sWUFBWSxRQUFRLE9BQU8sWUFBWSxNQUFNLE9BQU8sQ0FBQyxPQUFPO0FBQ2pFLHNCQUFNLGFBQWEsS0FBSyxHQUFHO0FBQzNCLHVCQUFPLENBQUMsV0FBVyxTQUFTLDRCQUE0QjtBQUFBLGNBQzFELENBQUM7QUFDRCwwQ0FBNEI7QUFBQSxZQUM5QjtBQUFBLFVBQ0Y7QUFBQSxRQUNGO0FBQUEsTUFDRjtBQUFBLE1BQ0EsNEJBQTRCO0FBQUEsUUFDMUIsTUFBTTtBQUFBLFFBQ04sb0JBQW9CO0FBQUEsVUFDbEIsU0FBUztBQUFBLFVBQ1QsVUFBVSxPQUFPLEVBQUUsTUFBQVEsT0FBTSxPQUFPLEdBQUc7QUFDakMsZ0JBQUlBLFVBQVMsdUJBQXVCO0FBQ2xDO0FBQUEsWUFDRjtBQUVBLG1CQUFPO0FBQUEsY0FDTDtBQUFBLGdCQUNFLEtBQUs7QUFBQSxnQkFDTCxPQUFPLEVBQUUsTUFBTSxVQUFVLEtBQUsscUNBQXFDO0FBQUEsZ0JBQ25FLFVBQVU7QUFBQSxjQUNaO0FBQUEsWUFDRjtBQUFBLFVBQ0Y7QUFBQSxRQUNGO0FBQUEsTUFDRjtBQUFBLE1BQ0E7QUFBQSxRQUNFLE1BQU07QUFBQSxRQUNOLG9CQUFvQjtBQUFBLFVBQ2xCLFNBQVM7QUFBQSxVQUNULFVBQVUsT0FBTyxFQUFFLE1BQUFBLE9BQU0sT0FBTyxHQUFHO0FBQ2pDLGdCQUFJQSxVQUFTLGVBQWU7QUFDMUI7QUFBQSxZQUNGO0FBRUEsa0JBQU0sVUFBVSxDQUFDO0FBRWpCLGdCQUFJLFNBQVM7QUFDWCxzQkFBUSxLQUFLO0FBQUEsZ0JBQ1gsS0FBSztBQUFBLGdCQUNMLE9BQU8sRUFBRSxNQUFNLFVBQVUsS0FBSyw2QkFBNkI7QUFBQSxnQkFDM0QsVUFBVTtBQUFBLGNBQ1osQ0FBQztBQUFBLFlBQ0g7QUFDQSxvQkFBUSxLQUFLO0FBQUEsY0FDWCxLQUFLO0FBQUEsY0FDTCxPQUFPLEVBQUUsTUFBTSxVQUFVLEtBQUssdUJBQXVCO0FBQUEsY0FDckQsVUFBVTtBQUFBLFlBQ1osQ0FBQztBQUNELG1CQUFPO0FBQUEsVUFDVDtBQUFBLFFBQ0Y7QUFBQSxNQUNGO0FBQUEsTUFDQSxRQUFRO0FBQUEsUUFDTixZQUFZO0FBQUEsTUFDZCxDQUFDO0FBQUEsTUFDRCxrQkFBa0IsV0FBVyxFQUFFLFlBQVksTUFBTSxVQUFVLGVBQWUsQ0FBQztBQUFBLElBQzdFO0FBQUEsRUFDRjtBQUNGO0FBRU8sSUFBTSx1QkFBdUIsQ0FBQ0Msa0JBQStCO0FBQ2xFLFNBQU8sYUFBYSxDQUFDLFFBQVEsWUFBWSxhQUFhLEdBQUcsR0FBR0EsY0FBYSxHQUFHLENBQUMsQ0FBQztBQUNoRjtBQUNBLFNBQVMsV0FBVyxRQUF3QjtBQUMxQyxRQUFNLGNBQWMsS0FBSyxRQUFRLG1CQUFtQixRQUFRLGNBQWM7QUFDMUUsU0FBTyxLQUFLLE1BQU1SLGNBQWEsYUFBYSxFQUFFLFVBQVUsUUFBUSxDQUFDLENBQUMsRUFBRTtBQUN0RTtBQUNBLFNBQVMsWUFBWSxRQUF3QjtBQUMzQyxRQUFNLGNBQWMsS0FBSyxRQUFRLG1CQUFtQixRQUFRLGNBQWM7QUFDMUUsU0FBTyxLQUFLLE1BQU1BLGNBQWEsYUFBYSxFQUFFLFVBQVUsUUFBUSxDQUFDLENBQUMsRUFBRTtBQUN0RTs7O0FPdnpCQSxJQUFNLGVBQTZCLENBQUMsU0FBUztBQUFBO0FBQUE7QUFHN0M7QUFFQSxJQUFPLHNCQUFRLHFCQUFxQixZQUFZOyIsCiAgIm5hbWVzIjogWyJleGlzdHNTeW5jIiwgIm1rZGlyU3luYyIsICJyZWFkZGlyU3luYyIsICJyZWFkRmlsZVN5bmMiLCAid3JpdGVGaWxlU3luYyIsICJleGlzdHNTeW5jIiwgInJlYWRGaWxlU3luYyIsICJyZXNvbHZlIiwgImdsb2JTeW5jIiwgInJlc29sdmUiLCAiYmFzZW5hbWUiLCAiZXhpc3RzU3luYyIsICJ0aGVtZUZvbGRlciIsICJ0aGVtZUZvbGRlciIsICJyZXNvbHZlIiwgImdsb2JTeW5jIiwgImV4aXN0c1N5bmMiLCAiYmFzZW5hbWUiLCAidmFyaWFibGUiLCAiZmlsZW5hbWUiLCAiZXhpc3RzU3luYyIsICJyZXNvbHZlIiwgInRoZW1lRm9sZGVyIiwgInJlYWRGaWxlU3luYyIsICJleGlzdHNTeW5jIiwgInJlYWRGaWxlU3luYyIsICJyZXNvbHZlIiwgImJhc2VuYW1lIiwgImdsb2JTeW5jIiwgInRoZW1lRm9sZGVyIiwgImdldFRoZW1lUHJvcGVydGllcyIsICJnbG9iU3luYyIsICJyZXNvbHZlIiwgImV4aXN0c1N5bmMiLCAicmVhZEZpbGVTeW5jIiwgInJlcGxhY2UiLCAiYmFzZW5hbWUiLCAicmVxdWlyZSIsICJleGlzdHNTeW5jIiwgInJlYWRGaWxlU3luYyIsICJta2RpclN5bmMiLCAiYnVuZGxlIiwgInJlYWRkaXJTeW5jIiwgInRoZW1lRm9sZGVyIiwgIndyaXRlRmlsZVN5bmMiLCAiZSIsICJwYXRoIiwgImN1c3RvbUNvbmZpZyJdCn0K
