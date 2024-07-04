(function(){const e=document.createElement("link").relList;if(e&&e.supports&&e.supports("modulepreload"))return;for(const i of document.querySelectorAll('link[rel="modulepreload"]'))n(i);new MutationObserver(i=>{for(const o of i)if(o.type==="childList")for(const s of o.addedNodes)s.tagName==="LINK"&&s.rel==="modulepreload"&&n(s)}).observe(document,{childList:!0,subtree:!0});function t(i){const o={};return i.integrity&&(o.integrity=i.integrity),i.referrerPolicy&&(o.referrerPolicy=i.referrerPolicy),i.crossOrigin==="use-credentials"?o.credentials="include":i.crossOrigin==="anonymous"?o.credentials="omit":o.credentials="same-origin",o}function n(i){if(i.ep)return;i.ep=!0;const o=t(i);fetch(i.href,o)}})();window.Vaadin=window.Vaadin||{};window.Vaadin.featureFlags=window.Vaadin.featureFlags||{};window.Vaadin.featureFlags.exampleFeatureFlag=!1;window.Vaadin.featureFlags.collaborationEngineBackend=!1;window.Vaadin.featureFlags.webPush=!1;window.Vaadin.featureFlags.formFillerAddon=!1;const Wr="modulepreload",Hr=function(r,e){return new URL(r,e).href},Bt={},gt=function(e,t,n){if(!t||t.length===0)return e();const i=document.getElementsByTagName("link");return Promise.all(t.map(o=>{if(o=Hr(o,n),o in Bt)return;Bt[o]=!0;const s=o.endsWith(".css"),c=s?'[rel="stylesheet"]':"";if(!!n)for(let m=i.length-1;m>=0;m--){const h=i[m];if(h.href===o&&(!s||h.rel==="stylesheet"))return}else if(document.querySelector(`link[href="${o}"]${c}`))return;const l=document.createElement("link");if(l.rel=s?"stylesheet":Wr,s||(l.as="script",l.crossOrigin=""),l.href=o,document.head.appendChild(l),s)return new Promise((m,h)=>{l.addEventListener("load",m),l.addEventListener("error",()=>h(new Error(`Unable to preload CSS for ${o}`)))})})).then(()=>e()).catch(o=>{const s=new Event("vite:preloadError",{cancelable:!0});if(s.payload=o,window.dispatchEvent(s),!s.defaultPrevented)throw o})};function Ae(r){return r=r||[],Array.isArray(r)?r:[r]}function A(r){return`[Vaadin.Router] ${r}`}function qr(r){if(typeof r!="object")return String(r);const e=Object.prototype.toString.call(r).match(/ (.*)\]$/)[1];return e==="Object"||e==="Array"?`${e} ${JSON.stringify(r)}`:e}const Le="module",Ie="nomodule",vt=[Le,Ie];function Wt(r){if(!r.match(/.+\.[m]?js$/))throw new Error(A(`Unsupported type for bundle "${r}": .js or .mjs expected.`))}function vr(r){if(!r||!O(r.path))throw new Error(A('Expected route config to be an object with a "path" string property, or an array of such objects'));const e=r.bundle,t=["component","redirect","bundle"];if(!Q(r.action)&&!Array.isArray(r.children)&&!Q(r.children)&&!Me(e)&&!t.some(n=>O(r[n])))throw new Error(A(`Expected route config "${r.path}" to include either "${t.join('", "')}" or "action" function but none found.`));if(e)if(O(e))Wt(e);else if(vt.some(n=>n in e))vt.forEach(n=>n in e&&Wt(e[n]));else throw new Error(A('Expected route bundle to include either "'+Ie+'" or "'+Le+'" keys, or both'));r.redirect&&["bundle","component"].forEach(n=>{n in r&&console.warn(A(`Route config "${r.path}" has both "redirect" and "${n}" properties, and "redirect" will always override the latter. Did you mean to only use "${n}"?`))})}function Ht(r){Ae(r).forEach(e=>vr(e))}function qt(r,e){let t=document.head.querySelector('script[src="'+r+'"][async]');return t||(t=document.createElement("script"),t.setAttribute("src",r),e===Le?t.setAttribute("type",Le):e===Ie&&t.setAttribute(Ie,""),t.async=!0),new Promise((n,i)=>{t.onreadystatechange=t.onload=o=>{t.__dynamicImportLoaded=!0,n(o)},t.onerror=o=>{t.parentNode&&t.parentNode.removeChild(t),i(o)},t.parentNode===null?document.head.appendChild(t):t.__dynamicImportLoaded&&n()})}function Gr(r){return O(r)?qt(r):Promise.race(vt.filter(e=>e in r).map(e=>qt(r[e],e)))}function pe(r,e){return!window.dispatchEvent(new CustomEvent(`vaadin-router-${r}`,{cancelable:r==="go",detail:e}))}function Me(r){return typeof r=="object"&&!!r}function Q(r){return typeof r=="function"}function O(r){return typeof r=="string"}function xr(r){const e=new Error(A(`Page not found (${r.pathname})`));return e.context=r,e.code=404,e}const ie=new class{};function Kr(r){const e=r.port,t=r.protocol,o=t==="http:"&&e==="80"||t==="https:"&&e==="443"?r.hostname:r.host;return`${t}//${o}`}function Gt(r){if(r.defaultPrevented||r.button!==0||r.shiftKey||r.ctrlKey||r.altKey||r.metaKey)return;let e=r.target;const t=r.composedPath?r.composedPath():r.path||[];for(let c=0;c<t.length;c++){const a=t[c];if(a.nodeName&&a.nodeName.toLowerCase()==="a"){e=a;break}}for(;e&&e.nodeName.toLowerCase()!=="a";)e=e.parentNode;if(!e||e.nodeName.toLowerCase()!=="a"||e.target&&e.target.toLowerCase()!=="_self"||e.hasAttribute("download")||e.hasAttribute("router-ignore")||e.pathname===window.location.pathname&&e.hash!==""||(e.origin||Kr(e))!==window.location.origin)return;const{pathname:i,search:o,hash:s}=e;pe("go",{pathname:i,search:o,hash:s})&&(r.preventDefault(),r&&r.type==="click"&&window.scrollTo(0,0))}const Qr={activate(){window.document.addEventListener("click",Gt)},inactivate(){window.document.removeEventListener("click",Gt)}},Jr=/Trident/.test(navigator.userAgent);Jr&&!Q(window.PopStateEvent)&&(window.PopStateEvent=function(r,e){e=e||{};var t=document.createEvent("Event");return t.initEvent(r,!!e.bubbles,!!e.cancelable),t.state=e.state||null,t},window.PopStateEvent.prototype=window.Event.prototype);function Kt(r){if(r.state==="vaadin-router-ignore")return;const{pathname:e,search:t,hash:n}=window.location;pe("go",{pathname:e,search:t,hash:n})}const Xr={activate(){window.addEventListener("popstate",Kt)},inactivate(){window.removeEventListener("popstate",Kt)}};var le=Er,Yr=Et,Zr=nn,en=br,tn=Sr,yr="/",_r="./",rn=new RegExp(["(\\\\.)","(?:\\:(\\w+)(?:\\(((?:\\\\.|[^\\\\()])+)\\))?|\\(((?:\\\\.|[^\\\\()])+)\\))([+*?])?"].join("|"),"g");function Et(r,e){for(var t=[],n=0,i=0,o="",s=e&&e.delimiter||yr,c=e&&e.delimiters||_r,a=!1,l;(l=rn.exec(r))!==null;){var m=l[0],h=l[1],u=l.index;if(o+=r.slice(i,u),i=u+m.length,h){o+=h[1],a=!0;continue}var f="",U=r[i],j=l[2],V=l[3],Be=l[4],z=l[5];if(!a&&o.length){var L=o.length-1;c.indexOf(o[L])>-1&&(f=o[L],o=o.slice(0,L))}o&&(t.push(o),o="",a=!1);var Z=f!==""&&U!==void 0&&U!==f,ee=z==="+"||z==="*",We=z==="?"||z==="*",D=f||s,be=V||Be;t.push({name:j||n++,prefix:f,delimiter:D,optional:We,repeat:ee,partial:Z,pattern:be?on(be):"[^"+B(D)+"]+?"})}return(o||i<r.length)&&t.push(o+r.substr(i)),t}function nn(r,e){return br(Et(r,e))}function br(r){for(var e=new Array(r.length),t=0;t<r.length;t++)typeof r[t]=="object"&&(e[t]=new RegExp("^(?:"+r[t].pattern+")$"));return function(n,i){for(var o="",s=i&&i.encode||encodeURIComponent,c=0;c<r.length;c++){var a=r[c];if(typeof a=="string"){o+=a;continue}var l=n?n[a.name]:void 0,m;if(Array.isArray(l)){if(!a.repeat)throw new TypeError('Expected "'+a.name+'" to not repeat, but got array');if(l.length===0){if(a.optional)continue;throw new TypeError('Expected "'+a.name+'" to not be empty')}for(var h=0;h<l.length;h++){if(m=s(l[h],a),!e[c].test(m))throw new TypeError('Expected all "'+a.name+'" to match "'+a.pattern+'"');o+=(h===0?a.prefix:a.delimiter)+m}continue}if(typeof l=="string"||typeof l=="number"||typeof l=="boolean"){if(m=s(String(l),a),!e[c].test(m))throw new TypeError('Expected "'+a.name+'" to match "'+a.pattern+'", but got "'+m+'"');o+=a.prefix+m;continue}if(a.optional){a.partial&&(o+=a.prefix);continue}throw new TypeError('Expected "'+a.name+'" to be '+(a.repeat?"an array":"a string"))}return o}}function B(r){return r.replace(/([.+*?=^!:${}()[\]|/\\])/g,"\\$1")}function on(r){return r.replace(/([=!:$/()])/g,"\\$1")}function wr(r){return r&&r.sensitive?"":"i"}function sn(r,e){if(!e)return r;var t=r.source.match(/\((?!\?)/g);if(t)for(var n=0;n<t.length;n++)e.push({name:n,prefix:null,delimiter:null,optional:!1,repeat:!1,partial:!1,pattern:null});return r}function an(r,e,t){for(var n=[],i=0;i<r.length;i++)n.push(Er(r[i],e,t).source);return new RegExp("(?:"+n.join("|")+")",wr(t))}function ln(r,e,t){return Sr(Et(r,t),e,t)}function Sr(r,e,t){t=t||{};for(var n=t.strict,i=t.start!==!1,o=t.end!==!1,s=B(t.delimiter||yr),c=t.delimiters||_r,a=[].concat(t.endsWith||[]).map(B).concat("$").join("|"),l=i?"^":"",m=r.length===0,h=0;h<r.length;h++){var u=r[h];if(typeof u=="string")l+=B(u),m=h===r.length-1&&c.indexOf(u[u.length-1])>-1;else{var f=u.repeat?"(?:"+u.pattern+")(?:"+B(u.delimiter)+"(?:"+u.pattern+"))*":u.pattern;e&&e.push(u),u.optional?u.partial?l+=B(u.prefix)+"("+f+")?":l+="(?:"+B(u.prefix)+"("+f+"))?":l+=B(u.prefix)+"("+f+")"}}return o?(n||(l+="(?:"+s+")?"),l+=a==="$"?"$":"(?="+a+")"):(n||(l+="(?:"+s+"(?="+a+"))?"),m||(l+="(?="+s+"|"+a+")")),new RegExp(l,wr(t))}function Er(r,e,t){return r instanceof RegExp?sn(r,e):Array.isArray(r)?an(r,e,t):ln(r,e,t)}le.parse=Yr;le.compile=Zr;le.tokensToFunction=en;le.tokensToRegExp=tn;const{hasOwnProperty:cn}=Object.prototype,xt=new Map;xt.set("|false",{keys:[],pattern:/(?:)/});function Qt(r){try{return decodeURIComponent(r)}catch{return r}}function dn(r,e,t,n,i){t=!!t;const o=`${r}|${t}`;let s=xt.get(o);if(!s){const l=[];s={keys:l,pattern:le(r,l,{end:t,strict:r===""})},xt.set(o,s)}const c=s.pattern.exec(e);if(!c)return null;const a=Object.assign({},i);for(let l=1;l<c.length;l++){const m=s.keys[l-1],h=m.name,u=c[l];(u!==void 0||!cn.call(a,h))&&(m.repeat?a[h]=u?u.split(m.delimiter).map(Qt):[]:a[h]=u&&Qt(u))}return{path:c[0],keys:(n||[]).concat(s.keys),params:a}}function Cr(r,e,t,n,i){let o,s,c=0,a=r.path||"";return a.charAt(0)==="/"&&(t&&(a=a.substr(1)),t=!0),{next(l){if(r===l)return{done:!0};const m=r.__children=r.__children||r.children;if(!o&&(o=dn(a,e,!m,n,i),o))return{done:!1,value:{route:r,keys:o.keys,params:o.params,path:o.path}};if(o&&m)for(;c<m.length;){if(!s){const u=m[c];u.parent=r;let f=o.path.length;f>0&&e.charAt(f)==="/"&&(f+=1),s=Cr(u,e.substr(f),t,o.keys,o.params)}const h=s.next(l);if(!h.done)return{done:!1,value:h.value};s=null,c++}return{done:!0}}}}function un(r){if(Q(r.route.action))return r.route.action(r)}function mn(r,e){let t=e;for(;t;)if(t=t.parent,t===r)return!0;return!1}function pn(r){let e=`Path '${r.pathname}' is not properly resolved due to an error.`;const t=(r.route||{}).path;return t&&(e+=` Resolution had failed on route: '${t}'`),e}function hn(r,e){const{route:t,path:n}=e;if(t&&!t.__synthetic){const i={path:n,route:t};if(!r.chain)r.chain=[];else if(t.parent){let o=r.chain.length;for(;o--&&r.chain[o].route&&r.chain[o].route!==t.parent;)r.chain.pop()}r.chain.push(i)}}class he{constructor(e,t={}){if(Object(e)!==e)throw new TypeError("Invalid routes");this.baseUrl=t.baseUrl||"",this.errorHandler=t.errorHandler,this.resolveRoute=t.resolveRoute||un,this.context=Object.assign({resolver:this},t.context),this.root=Array.isArray(e)?{path:"",__children:e,parent:null,__synthetic:!0}:e,this.root.parent=null}getRoutes(){return[...this.root.__children]}setRoutes(e){Ht(e);const t=[...Ae(e)];this.root.__children=t}addRoutes(e){return Ht(e),this.root.__children.push(...Ae(e)),this.getRoutes()}removeRoutes(){this.setRoutes([])}resolve(e){const t=Object.assign({},this.context,O(e)?{pathname:e}:e),n=Cr(this.root,this.__normalizePathname(t.pathname),this.baseUrl),i=this.resolveRoute;let o=null,s=null,c=t;function a(l,m=o.value.route,h){const u=h===null&&o.value.route;return o=s||n.next(u),s=null,!l&&(o.done||!mn(m,o.value.route))?(s=o,Promise.resolve(ie)):o.done?Promise.reject(xr(t)):(c=Object.assign(c?{chain:c.chain?c.chain.slice(0):[]}:{},t,o.value),hn(c,o.value),Promise.resolve(i(c)).then(f=>f!=null&&f!==ie?(c.result=f.result||f,c):a(l,m,f)))}return t.next=a,Promise.resolve().then(()=>a(!0,this.root)).catch(l=>{const m=pn(c);if(l?console.warn(m):l=new Error(m),l.context=l.context||c,l instanceof DOMException||(l.code=l.code||500),this.errorHandler)return c.result=this.errorHandler(l),c;throw l})}static __createUrl(e,t){return new URL(e,t)}get __effectiveBaseUrl(){return this.baseUrl?this.constructor.__createUrl(this.baseUrl,document.baseURI||document.URL).href.replace(/[^\/]*$/,""):""}__normalizePathname(e){if(!this.baseUrl)return e;const t=this.__effectiveBaseUrl,n=this.constructor.__createUrl(e,t).href;if(n.slice(0,t.length)===t)return n.slice(t.length)}}he.pathToRegexp=le;const{pathToRegexp:Jt}=he,Xt=new Map;function Tr(r,e,t){const n=e.name||e.component;if(n&&(r.has(n)?r.get(n).push(e):r.set(n,[e])),Array.isArray(t))for(let i=0;i<t.length;i++){const o=t[i];o.parent=e,Tr(r,o,o.__children||o.children)}}function Yt(r,e){const t=r.get(e);if(t&&t.length>1)throw new Error(`Duplicate route with name "${e}". Try seting unique 'name' route properties.`);return t&&t[0]}function Zt(r){let e=r.path;return e=Array.isArray(e)?e[0]:e,e!==void 0?e:""}function fn(r,e={}){if(!(r instanceof he))throw new TypeError("An instance of Resolver is expected");const t=new Map;return(n,i)=>{let o=Yt(t,n);if(!o&&(t.clear(),Tr(t,r.root,r.root.__children),o=Yt(t,n),!o))throw new Error(`Route "${n}" not found`);let s=Xt.get(o.fullPath);if(!s){let a=Zt(o),l=o.parent;for(;l;){const f=Zt(l);f&&(a=f.replace(/\/$/,"")+"/"+a.replace(/^\//,"")),l=l.parent}const m=Jt.parse(a),h=Jt.tokensToFunction(m),u=Object.create(null);for(let f=0;f<m.length;f++)O(m[f])||(u[m[f].name]=!0);s={toPath:h,keys:u},Xt.set(a,s),o.fullPath=a}let c=s.toPath(i,e)||"/";if(e.stringifyQueryParams&&i){const a={},l=Object.keys(i);for(let h=0;h<l.length;h++){const u=l[h];s.keys[u]||(a[u]=i[u])}const m=e.stringifyQueryParams(a);m&&(c+=m.charAt(0)==="?"?m:`?${m}`)}return c}}let er=[];function gn(r){er.forEach(e=>e.inactivate()),r.forEach(e=>e.activate()),er=r}const vn=r=>{const e=getComputedStyle(r).getPropertyValue("animation-name");return e&&e!=="none"},xn=(r,e)=>{const t=()=>{r.removeEventListener("animationend",t),e()};r.addEventListener("animationend",t)};function tr(r,e){return r.classList.add(e),new Promise(t=>{if(vn(r)){const n=r.getBoundingClientRect(),i=`height: ${n.bottom-n.top}px; width: ${n.right-n.left}px`;r.setAttribute("style",`position: absolute; ${i}`),xn(r,()=>{r.classList.remove(e),r.removeAttribute("style"),t()})}else r.classList.remove(e),t()})}const yn=256;function Ke(r){return r!=null}function _n(r){const e=Object.assign({},r);return delete e.next,e}function N({pathname:r="",search:e="",hash:t="",chain:n=[],params:i={},redirectFrom:o,resolver:s},c){const a=n.map(l=>l.route);return{baseUrl:s&&s.baseUrl||"",pathname:r,search:e,hash:t,routes:a,route:c||a.length&&a[a.length-1]||null,params:i,redirectFrom:o,getUrl:(l={})=>ze(W.pathToRegexp.compile($r(a))(Object.assign({},i,l)),s)}}function rr(r,e){const t=Object.assign({},r.params);return{redirect:{pathname:e,from:r.pathname,params:t}}}function bn(r,e){e.location=N(r);const t=r.chain.map(n=>n.route).indexOf(r.route);return r.chain[t].element=e,e}function Pe(r,e,t){if(Q(r))return r.apply(t,e)}function nr(r,e,t){return n=>{if(n&&(n.cancel||n.redirect))return n;if(t)return Pe(t[r],e,t)}}function wn(r,e){if(!Array.isArray(r)&&!Me(r))throw new Error(A(`Incorrect "children" value for the route ${e.path}: expected array or object, but got ${r}`));e.__children=[];const t=Ae(r);for(let n=0;n<t.length;n++)vr(t[n]),e.__children.push(t[n])}function Te(r){if(r&&r.length){const e=r[0].parentNode;for(let t=0;t<r.length;t++)e.removeChild(r[t])}}function ze(r,e){const t=e.__effectiveBaseUrl;return t?e.constructor.__createUrl(r.replace(/^\//,""),t).pathname:r}function $r(r){return r.map(e=>e.path).reduce((e,t)=>t.length?e.replace(/\/$/,"")+"/"+t.replace(/^\//,""):e,"")}class W extends he{constructor(e,t){const n=document.head.querySelector("base"),i=n&&n.getAttribute("href");super([],Object.assign({baseUrl:i&&he.__createUrl(i,document.URL).pathname.replace(/[^\/]*$/,"")},t)),this.resolveRoute=s=>this.__resolveRoute(s);const o=W.NavigationTrigger;W.setTriggers.apply(W,Object.keys(o).map(s=>o[s])),this.baseUrl,this.ready,this.ready=Promise.resolve(e),this.location,this.location=N({resolver:this}),this.__lastStartedRenderId=0,this.__navigationEventHandler=this.__onNavigationEvent.bind(this),this.setOutlet(e),this.subscribe(),this.__createdByRouter=new WeakMap,this.__addedByRouter=new WeakMap}__resolveRoute(e){const t=e.route;let n=Promise.resolve();Q(t.children)&&(n=n.then(()=>t.children(_n(e))).then(o=>{!Ke(o)&&!Q(t.children)&&(o=t.children),wn(o,t)}));const i={redirect:o=>rr(e,o),component:o=>{const s=document.createElement(o);return this.__createdByRouter.set(s,!0),s}};return n.then(()=>{if(this.__isLatestRender(e))return Pe(t.action,[e,i],t)}).then(o=>{if(Ke(o)&&(o instanceof HTMLElement||o.redirect||o===ie))return o;if(O(t.redirect))return i.redirect(t.redirect);if(t.bundle)return Gr(t.bundle).then(()=>{},()=>{throw new Error(A(`Bundle not found: ${t.bundle}. Check if the file name is correct`))})}).then(o=>{if(Ke(o))return o;if(O(t.component))return i.component(t.component)})}setOutlet(e){e&&this.__ensureOutlet(e),this.__outlet=e}getOutlet(){return this.__outlet}setRoutes(e,t=!1){return this.__previousContext=void 0,this.__urlForName=void 0,super.setRoutes(e),t||this.__onNavigationEvent(),this.ready}render(e,t){const n=++this.__lastStartedRenderId,i=Object.assign({search:"",hash:""},O(e)?{pathname:e}:e,{__renderId:n});return this.ready=this.resolve(i).then(o=>this.__fullyResolveChain(o)).then(o=>{if(this.__isLatestRender(o)){const s=this.__previousContext;if(o===s)return this.__updateBrowserHistory(s,!0),this.location;if(this.location=N(o),t&&this.__updateBrowserHistory(o,n===1),pe("location-changed",{router:this,location:this.location}),o.__skipAttach)return this.__copyUnchangedElements(o,s),this.__previousContext=o,this.location;this.__addAppearingContent(o,s);const c=this.__animateIfNeeded(o);return this.__runOnAfterEnterCallbacks(o),this.__runOnAfterLeaveCallbacks(o,s),c.then(()=>{if(this.__isLatestRender(o))return this.__removeDisappearingContent(),this.__previousContext=o,this.location})}}).catch(o=>{if(n===this.__lastStartedRenderId)throw t&&this.__updateBrowserHistory(i),Te(this.__outlet&&this.__outlet.children),this.location=N(Object.assign(i,{resolver:this})),pe("error",Object.assign({router:this,error:o},i)),o}),this.ready}__fullyResolveChain(e,t=e){return this.__findComponentContextAfterAllRedirects(t).then(n=>{const o=n!==t?n:e,c=ze($r(n.chain),n.resolver)===n.pathname,a=(l,m=l.route,h)=>l.next(void 0,m,h).then(u=>u===null||u===ie?c?l:m.parent!==null?a(l,m.parent,u):u:u);return a(n).then(l=>{if(l===null||l===ie)throw xr(o);return l&&l!==ie&&l!==n?this.__fullyResolveChain(o,l):this.__amendWithOnBeforeCallbacks(n)})})}__findComponentContextAfterAllRedirects(e){const t=e.result;return t instanceof HTMLElement?(bn(e,t),Promise.resolve(e)):t.redirect?this.__redirect(t.redirect,e.__redirectCount,e.__renderId).then(n=>this.__findComponentContextAfterAllRedirects(n)):t instanceof Error?Promise.reject(t):Promise.reject(new Error(A(`Invalid route resolution result for path "${e.pathname}". Expected redirect object or HTML element, but got: "${qr(t)}". Double check the action return value for the route.`)))}__amendWithOnBeforeCallbacks(e){return this.__runOnBeforeCallbacks(e).then(t=>t===this.__previousContext||t===e?t:this.__fullyResolveChain(t))}__runOnBeforeCallbacks(e){const t=this.__previousContext||{},n=t.chain||[],i=e.chain;let o=Promise.resolve();const s=()=>({cancel:!0}),c=a=>rr(e,a);if(e.__divergedChainIndex=0,e.__skipAttach=!1,n.length){for(let a=0;a<Math.min(n.length,i.length)&&!(n[a].route!==i[a].route||n[a].path!==i[a].path&&n[a].element!==i[a].element||!this.__isReusableElement(n[a].element,i[a].element));a=++e.__divergedChainIndex);if(e.__skipAttach=i.length===n.length&&e.__divergedChainIndex==i.length&&this.__isReusableElement(e.result,t.result),e.__skipAttach){for(let a=i.length-1;a>=0;a--)o=this.__runOnBeforeLeaveCallbacks(o,e,{prevent:s},n[a]);for(let a=0;a<i.length;a++)o=this.__runOnBeforeEnterCallbacks(o,e,{prevent:s,redirect:c},i[a]),n[a].element.location=N(e,n[a].route)}else for(let a=n.length-1;a>=e.__divergedChainIndex;a--)o=this.__runOnBeforeLeaveCallbacks(o,e,{prevent:s},n[a])}if(!e.__skipAttach)for(let a=0;a<i.length;a++)a<e.__divergedChainIndex?a<n.length&&n[a].element&&(n[a].element.location=N(e,n[a].route)):(o=this.__runOnBeforeEnterCallbacks(o,e,{prevent:s,redirect:c},i[a]),i[a].element&&(i[a].element.location=N(e,i[a].route)));return o.then(a=>{if(a){if(a.cancel)return this.__previousContext.__renderId=e.__renderId,this.__previousContext;if(a.redirect)return this.__redirect(a.redirect,e.__redirectCount,e.__renderId)}return e})}__runOnBeforeLeaveCallbacks(e,t,n,i){const o=N(t);return e.then(s=>{if(this.__isLatestRender(t))return nr("onBeforeLeave",[o,n,this],i.element)(s)}).then(s=>{if(!(s||{}).redirect)return s})}__runOnBeforeEnterCallbacks(e,t,n,i){const o=N(t,i.route);return e.then(s=>{if(this.__isLatestRender(t))return nr("onBeforeEnter",[o,n,this],i.element)(s)})}__isReusableElement(e,t){return e&&t?this.__createdByRouter.get(e)&&this.__createdByRouter.get(t)?e.localName===t.localName:e===t:!1}__isLatestRender(e){return e.__renderId===this.__lastStartedRenderId}__redirect(e,t,n){if(t>yn)throw new Error(A(`Too many redirects when rendering ${e.from}`));return this.resolve({pathname:this.urlForPath(e.pathname,e.params),redirectFrom:e.from,__redirectCount:(t||0)+1,__renderId:n})}__ensureOutlet(e=this.__outlet){if(!(e instanceof Node))throw new TypeError(A(`Expected router outlet to be a valid DOM Node (but got ${e})`))}__updateBrowserHistory({pathname:e,search:t="",hash:n=""},i){if(window.location.pathname!==e||window.location.search!==t||window.location.hash!==n){const o=i?"replaceState":"pushState";window.history[o](null,document.title,e+t+n),window.dispatchEvent(new PopStateEvent("popstate",{state:"vaadin-router-ignore"}))}}__copyUnchangedElements(e,t){let n=this.__outlet;for(let i=0;i<e.__divergedChainIndex;i++){const o=t&&t.chain[i].element;if(o)if(o.parentNode===n)e.chain[i].element=o,n=o;else break}return n}__addAppearingContent(e,t){this.__ensureOutlet(),this.__removeAppearingContent();const n=this.__copyUnchangedElements(e,t);this.__appearingContent=[],this.__disappearingContent=Array.from(n.children).filter(o=>this.__addedByRouter.get(o)&&o!==e.result);let i=n;for(let o=e.__divergedChainIndex;o<e.chain.length;o++){const s=e.chain[o].element;s&&(i.appendChild(s),this.__addedByRouter.set(s,!0),i===n&&this.__appearingContent.push(s),i=s)}}__removeDisappearingContent(){this.__disappearingContent&&Te(this.__disappearingContent),this.__disappearingContent=null,this.__appearingContent=null}__removeAppearingContent(){this.__disappearingContent&&this.__appearingContent&&(Te(this.__appearingContent),this.__disappearingContent=null,this.__appearingContent=null)}__runOnAfterLeaveCallbacks(e,t){if(t)for(let n=t.chain.length-1;n>=e.__divergedChainIndex&&this.__isLatestRender(e);n--){const i=t.chain[n].element;if(i)try{const o=N(e);Pe(i.onAfterLeave,[o,{},t.resolver],i)}finally{this.__disappearingContent.indexOf(i)>-1&&Te(i.children)}}}__runOnAfterEnterCallbacks(e){for(let t=e.__divergedChainIndex;t<e.chain.length&&this.__isLatestRender(e);t++){const n=e.chain[t].element||{},i=N(e,e.chain[t].route);Pe(n.onAfterEnter,[i,{},e.resolver],n)}}__animateIfNeeded(e){const t=(this.__disappearingContent||[])[0],n=(this.__appearingContent||[])[0],i=[],o=e.chain;let s;for(let c=o.length;c>0;c--)if(o[c-1].route.animate){s=o[c-1].route.animate;break}if(t&&n&&s){const c=Me(s)&&s.leave||"leaving",a=Me(s)&&s.enter||"entering";i.push(tr(t,c)),i.push(tr(n,a))}return Promise.all(i).then(()=>e)}subscribe(){window.addEventListener("vaadin-router-go",this.__navigationEventHandler)}unsubscribe(){window.removeEventListener("vaadin-router-go",this.__navigationEventHandler)}__onNavigationEvent(e){const{pathname:t,search:n,hash:i}=e?e.detail:window.location;O(this.__normalizePathname(t))&&(e&&e.preventDefault&&e.preventDefault(),this.render({pathname:t,search:n,hash:i},!0))}static setTriggers(...e){gn(e)}urlForName(e,t){return this.__urlForName||(this.__urlForName=fn(this)),ze(this.__urlForName(e,t),this)}urlForPath(e,t){return ze(W.pathToRegexp.compile(e)(t),this)}static go(e){const{pathname:t,search:n,hash:i}=O(e)?this.__createUrl(e,"http://a"):e;return pe("go",{pathname:t,search:n,hash:i})}}const Sn=/\/\*[\*!]\s+vaadin-dev-mode:start([\s\S]*)vaadin-dev-mode:end\s+\*\*\//i,Re=window.Vaadin&&window.Vaadin.Flow&&window.Vaadin.Flow.clients;function En(){function r(){return!0}return Pr(r)}function Cn(){try{return Tn()?!0:$n()?Re?!Pn():!En():!1}catch{return!1}}function Tn(){return localStorage.getItem("vaadin.developmentmode.force")}function $n(){return["localhost","127.0.0.1"].indexOf(window.location.hostname)>=0}function Pn(){return!!(Re&&Object.keys(Re).map(e=>Re[e]).filter(e=>e.productionMode).length>0)}function Pr(r,e){if(typeof r!="function")return;const t=Sn.exec(r.toString());if(t)try{r=new Function(t[1])}catch(n){console.log("vaadin-development-mode-detector: uncommentAndRun() failed",n)}return r(e)}window.Vaadin=window.Vaadin||{};const or=function(r,e){if(window.Vaadin.developmentMode)return Pr(r,e)};window.Vaadin.developmentMode===void 0&&(window.Vaadin.developmentMode=Cn());function zn(){}const Rn=function(){if(typeof or=="function")return or(zn)};window.Vaadin=window.Vaadin||{};window.Vaadin.registrations=window.Vaadin.registrations||[];window.Vaadin.registrations.push({is:"@vaadin/router",version:"1.7.4"});Rn();W.NavigationTrigger={POPSTATE:Xr,CLICK:Qr};var Qe,_;(function(r){r.CONNECTED="connected",r.LOADING="loading",r.RECONNECTING="reconnecting",r.CONNECTION_LOST="connection-lost"})(_||(_={}));class Nn{constructor(e){this.stateChangeListeners=new Set,this.loadingCount=0,this.connectionState=e,this.serviceWorkerMessageListener=this.serviceWorkerMessageListener.bind(this),navigator.serviceWorker&&(navigator.serviceWorker.addEventListener("message",this.serviceWorkerMessageListener),navigator.serviceWorker.ready.then(t=>{var n;(n=t==null?void 0:t.active)===null||n===void 0||n.postMessage({method:"Vaadin.ServiceWorker.isConnectionLost",id:"Vaadin.ServiceWorker.isConnectionLost"})}))}addStateChangeListener(e){this.stateChangeListeners.add(e)}removeStateChangeListener(e){this.stateChangeListeners.delete(e)}loadingStarted(){this.state=_.LOADING,this.loadingCount+=1}loadingFinished(){this.decreaseLoadingCount(_.CONNECTED)}loadingFailed(){this.decreaseLoadingCount(_.CONNECTION_LOST)}decreaseLoadingCount(e){this.loadingCount>0&&(this.loadingCount-=1,this.loadingCount===0&&(this.state=e))}get state(){return this.connectionState}set state(e){if(e!==this.connectionState){const t=this.connectionState;this.connectionState=e,this.loadingCount=0;for(const n of this.stateChangeListeners)n(t,this.connectionState)}}get online(){return this.connectionState===_.CONNECTED||this.connectionState===_.LOADING}get offline(){return!this.online}serviceWorkerMessageListener(e){typeof e.data=="object"&&e.data.id==="Vaadin.ServiceWorker.isConnectionLost"&&(e.data.result===!0&&(this.state=_.CONNECTION_LOST),navigator.serviceWorker.removeEventListener("message",this.serviceWorkerMessageListener))}}const kn=r=>!!(r==="localhost"||r==="[::1]"||r.match(/^127\.\d+\.\d+\.\d+$/)),$e=window;if(!(!((Qe=$e.Vaadin)===null||Qe===void 0)&&Qe.connectionState)){let r;kn(window.location.hostname)?r=!0:r=navigator.onLine,$e.Vaadin=$e.Vaadin||{},$e.Vaadin.connectionState=new Nn(r?_.CONNECTED:_.CONNECTION_LOST)}function $(r,e,t,n){var i=arguments.length,o=i<3?e:n===null?n=Object.getOwnPropertyDescriptor(e,t):n,s;if(typeof Reflect=="object"&&typeof Reflect.decorate=="function")o=Reflect.decorate(r,e,t,n);else for(var c=r.length-1;c>=0;c--)(s=r[c])&&(o=(i<3?s(o):i>3?s(e,t,o):s(e,t))||o);return i>3&&o&&Object.defineProperty(e,t,o),o}/**
 * @license
 * Copyright 2019 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const On=!1,Ne=window,Ct=Ne.ShadowRoot&&(Ne.ShadyCSS===void 0||Ne.ShadyCSS.nativeShadow)&&"adoptedStyleSheets"in Document.prototype&&"replace"in CSSStyleSheet.prototype,Tt=Symbol(),ir=new WeakMap;class $t{constructor(e,t,n){if(this._$cssResult$=!0,n!==Tt)throw new Error("CSSResult is not constructable. Use `unsafeCSS` or `css` instead.");this.cssText=e,this._strings=t}get styleSheet(){let e=this._styleSheet;const t=this._strings;if(Ct&&e===void 0){const n=t!==void 0&&t.length===1;n&&(e=ir.get(t)),e===void 0&&((this._styleSheet=e=new CSSStyleSheet).replaceSync(this.cssText),n&&ir.set(t,e))}return e}toString(){return this.cssText}}const An=r=>{if(r._$cssResult$===!0)return r.cssText;if(typeof r=="number")return r;throw new Error(`Value passed to 'css' function must be a 'css' function result: ${r}. Use 'unsafeCSS' to pass non-literal values, but take care to ensure page security.`)},Pt=r=>new $t(typeof r=="string"?r:String(r),void 0,Tt),y=(r,...e)=>{const t=r.length===1?r[0]:e.reduce((n,i,o)=>n+An(i)+r[o+1],r[0]);return new $t(t,r,Tt)},Ln=(r,e)=>{Ct?r.adoptedStyleSheets=e.map(t=>t instanceof CSSStyleSheet?t:t.styleSheet):e.forEach(t=>{const n=document.createElement("style"),i=Ne.litNonce;i!==void 0&&n.setAttribute("nonce",i),n.textContent=t.cssText,r.appendChild(n)})},In=r=>{let e="";for(const t of r.cssRules)e+=t.cssText;return Pt(e)},sr=Ct||On?r=>r:r=>r instanceof CSSStyleSheet?In(r):r;/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var Je,Xe,Ye,zr;const I=window;let Rr,H;const ar=I.trustedTypes,Mn=ar?ar.emptyScript:"",ke=I.reactiveElementPolyfillSupportDevMode;{const r=(Je=I.litIssuedWarnings)!==null&&Je!==void 0?Je:I.litIssuedWarnings=new Set;H=(e,t)=>{t+=` See https://lit.dev/msg/${e} for more information.`,r.has(t)||(console.warn(t),r.add(t))},H("dev-mode","Lit is in dev mode. Not recommended for production!"),!((Xe=I.ShadyDOM)===null||Xe===void 0)&&Xe.inUse&&ke===void 0&&H("polyfill-support-missing","Shadow DOM is being polyfilled via `ShadyDOM` but the `polyfill-support` module has not been loaded."),Rr=e=>({then:(t,n)=>{H("request-update-promise",`The \`requestUpdate\` method should no longer return a Promise but does so on \`${e}\`. Use \`updateComplete\` instead.`),t!==void 0&&t(!1)}})}const Ze=r=>{I.emitLitDebugLogEvents&&I.dispatchEvent(new CustomEvent("lit-debug",{detail:r}))},Nr=(r,e)=>r,yt={toAttribute(r,e){switch(e){case Boolean:r=r?Mn:null;break;case Object:case Array:r=r==null?r:JSON.stringify(r);break}return r},fromAttribute(r,e){let t=r;switch(e){case Boolean:t=r!==null;break;case Number:t=r===null?null:Number(r);break;case Object:case Array:try{t=JSON.parse(r)}catch{t=null}break}return t}},kr=(r,e)=>e!==r&&(e===e||r===r),et={attribute:!0,type:String,converter:yt,reflect:!1,hasChanged:kr},_t="finalized";class M extends HTMLElement{constructor(){super(),this.__instanceProperties=new Map,this.isUpdatePending=!1,this.hasUpdated=!1,this.__reflectingProperty=null,this.__initialize()}static addInitializer(e){var t;this.finalize(),((t=this._initializers)!==null&&t!==void 0?t:this._initializers=[]).push(e)}static get observedAttributes(){this.finalize();const e=[];return this.elementProperties.forEach((t,n)=>{const i=this.__attributeNameForProperty(n,t);i!==void 0&&(this.__attributeToPropertyMap.set(i,n),e.push(i))}),e}static createProperty(e,t=et){var n;if(t.state&&(t.attribute=!1),this.finalize(),this.elementProperties.set(e,t),!t.noAccessor&&!this.prototype.hasOwnProperty(e)){const i=typeof e=="symbol"?Symbol():`__${e}`,o=this.getPropertyDescriptor(e,i,t);o!==void 0&&(Object.defineProperty(this.prototype,e,o),this.hasOwnProperty("__reactivePropertyKeys")||(this.__reactivePropertyKeys=new Set((n=this.__reactivePropertyKeys)!==null&&n!==void 0?n:[])),this.__reactivePropertyKeys.add(e))}}static getPropertyDescriptor(e,t,n){return{get(){return this[t]},set(i){const o=this[e];this[t]=i,this.requestUpdate(e,o,n)},configurable:!0,enumerable:!0}}static getPropertyOptions(e){return this.elementProperties.get(e)||et}static finalize(){if(this.hasOwnProperty(_t))return!1;this[_t]=!0;const e=Object.getPrototypeOf(this);if(e.finalize(),e._initializers!==void 0&&(this._initializers=[...e._initializers]),this.elementProperties=new Map(e.elementProperties),this.__attributeToPropertyMap=new Map,this.hasOwnProperty(Nr("properties"))){const t=this.properties,n=[...Object.getOwnPropertyNames(t),...Object.getOwnPropertySymbols(t)];for(const i of n)this.createProperty(i,t[i])}this.elementStyles=this.finalizeStyles(this.styles);{const t=(n,i=!1)=>{this.prototype.hasOwnProperty(n)&&H(i?"renamed-api":"removed-api",`\`${n}\` is implemented on class ${this.name}. It has been ${i?"renamed":"removed"} in this version of LitElement.`)};t("initialize"),t("requestUpdateInternal"),t("_getUpdateComplete",!0)}return!0}static finalizeStyles(e){const t=[];if(Array.isArray(e)){const n=new Set(e.flat(1/0).reverse());for(const i of n)t.unshift(sr(i))}else e!==void 0&&t.push(sr(e));return t}static __attributeNameForProperty(e,t){const n=t.attribute;return n===!1?void 0:typeof n=="string"?n:typeof e=="string"?e.toLowerCase():void 0}__initialize(){var e;this.__updatePromise=new Promise(t=>this.enableUpdating=t),this._$changedProperties=new Map,this.__saveInstanceProperties(),this.requestUpdate(),(e=this.constructor._initializers)===null||e===void 0||e.forEach(t=>t(this))}addController(e){var t,n;((t=this.__controllers)!==null&&t!==void 0?t:this.__controllers=[]).push(e),this.renderRoot!==void 0&&this.isConnected&&((n=e.hostConnected)===null||n===void 0||n.call(e))}removeController(e){var t;(t=this.__controllers)===null||t===void 0||t.splice(this.__controllers.indexOf(e)>>>0,1)}__saveInstanceProperties(){this.constructor.elementProperties.forEach((e,t)=>{this.hasOwnProperty(t)&&(this.__instanceProperties.set(t,this[t]),delete this[t])})}createRenderRoot(){var e;const t=(e=this.shadowRoot)!==null&&e!==void 0?e:this.attachShadow(this.constructor.shadowRootOptions);return Ln(t,this.constructor.elementStyles),t}connectedCallback(){var e;this.renderRoot===void 0&&(this.renderRoot=this.createRenderRoot()),this.enableUpdating(!0),(e=this.__controllers)===null||e===void 0||e.forEach(t=>{var n;return(n=t.hostConnected)===null||n===void 0?void 0:n.call(t)})}enableUpdating(e){}disconnectedCallback(){var e;(e=this.__controllers)===null||e===void 0||e.forEach(t=>{var n;return(n=t.hostDisconnected)===null||n===void 0?void 0:n.call(t)})}attributeChangedCallback(e,t,n){this._$attributeToProperty(e,n)}__propertyToAttribute(e,t,n=et){var i;const o=this.constructor.__attributeNameForProperty(e,n);if(o!==void 0&&n.reflect===!0){const c=(((i=n.converter)===null||i===void 0?void 0:i.toAttribute)!==void 0?n.converter:yt).toAttribute(t,n.type);this.constructor.enabledWarnings.indexOf("migration")>=0&&c===void 0&&H("undefined-attribute-value",`The attribute value for the ${e} property is undefined on element ${this.localName}. The attribute will be removed, but in the previous version of \`ReactiveElement\`, the attribute would not have changed.`),this.__reflectingProperty=e,c==null?this.removeAttribute(o):this.setAttribute(o,c),this.__reflectingProperty=null}}_$attributeToProperty(e,t){var n;const i=this.constructor,o=i.__attributeToPropertyMap.get(e);if(o!==void 0&&this.__reflectingProperty!==o){const s=i.getPropertyOptions(o),c=typeof s.converter=="function"?{fromAttribute:s.converter}:((n=s.converter)===null||n===void 0?void 0:n.fromAttribute)!==void 0?s.converter:yt;this.__reflectingProperty=o,this[o]=c.fromAttribute(t,s.type),this.__reflectingProperty=null}}requestUpdate(e,t,n){let i=!0;return e!==void 0&&(n=n||this.constructor.getPropertyOptions(e),(n.hasChanged||kr)(this[e],t)?(this._$changedProperties.has(e)||this._$changedProperties.set(e,t),n.reflect===!0&&this.__reflectingProperty!==e&&(this.__reflectingProperties===void 0&&(this.__reflectingProperties=new Map),this.__reflectingProperties.set(e,n))):i=!1),!this.isUpdatePending&&i&&(this.__updatePromise=this.__enqueueUpdate()),Rr(this.localName)}async __enqueueUpdate(){this.isUpdatePending=!0;try{await this.__updatePromise}catch(t){Promise.reject(t)}const e=this.scheduleUpdate();return e!=null&&await e,!this.isUpdatePending}scheduleUpdate(){return this.performUpdate()}performUpdate(){var e,t;if(!this.isUpdatePending)return;if(Ze==null||Ze({kind:"update"}),!this.hasUpdated){const o=[];if((e=this.constructor.__reactivePropertyKeys)===null||e===void 0||e.forEach(s=>{var c;this.hasOwnProperty(s)&&!(!((c=this.__instanceProperties)===null||c===void 0)&&c.has(s))&&o.push(s)}),o.length)throw new Error(`The following properties on element ${this.localName} will not trigger updates as expected because they are set using class fields: ${o.join(", ")}. Native class fields and some compiled output will overwrite accessors used for detecting changes. See https://lit.dev/msg/class-field-shadowing for more information.`)}this.__instanceProperties&&(this.__instanceProperties.forEach((o,s)=>this[s]=o),this.__instanceProperties=void 0);let n=!1;const i=this._$changedProperties;try{n=this.shouldUpdate(i),n?(this.willUpdate(i),(t=this.__controllers)===null||t===void 0||t.forEach(o=>{var s;return(s=o.hostUpdate)===null||s===void 0?void 0:s.call(o)}),this.update(i)):this.__markUpdated()}catch(o){throw n=!1,this.__markUpdated(),o}n&&this._$didUpdate(i)}willUpdate(e){}_$didUpdate(e){var t;(t=this.__controllers)===null||t===void 0||t.forEach(n=>{var i;return(i=n.hostUpdated)===null||i===void 0?void 0:i.call(n)}),this.hasUpdated||(this.hasUpdated=!0,this.firstUpdated(e)),this.updated(e),this.isUpdatePending&&this.constructor.enabledWarnings.indexOf("change-in-update")>=0&&H("change-in-update",`Element ${this.localName} scheduled an update (generally because a property was set) after an update completed, causing a new update to be scheduled. This is inefficient and should be avoided unless the next update can only be scheduled as a side effect of the previous update.`)}__markUpdated(){this._$changedProperties=new Map,this.isUpdatePending=!1}get updateComplete(){return this.getUpdateComplete()}getUpdateComplete(){return this.__updatePromise}shouldUpdate(e){return!0}update(e){this.__reflectingProperties!==void 0&&(this.__reflectingProperties.forEach((t,n)=>this.__propertyToAttribute(n,this[n],t)),this.__reflectingProperties=void 0),this.__markUpdated()}updated(e){}firstUpdated(e){}}zr=_t;M[zr]=!0;M.elementProperties=new Map;M.elementStyles=[];M.shadowRootOptions={mode:"open"};ke==null||ke({ReactiveElement:M});{M.enabledWarnings=["change-in-update"];const r=function(e){e.hasOwnProperty(Nr("enabledWarnings"))||(e.enabledWarnings=e.enabledWarnings.slice())};M.enableWarning=function(e){r(this),this.enabledWarnings.indexOf(e)<0&&this.enabledWarnings.push(e)},M.disableWarning=function(e){r(this);const t=this.enabledWarnings.indexOf(e);t>=0&&this.enabledWarnings.splice(t,1)}}((Ye=I.reactiveElementVersions)!==null&&Ye!==void 0?Ye:I.reactiveElementVersions=[]).push("1.6.3");I.reactiveElementVersions.length>1&&H("multiple-versions","Multiple versions of Lit loaded. Loading multiple versions is not recommended.");/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var tt,rt,nt,ot;const T=window,g=r=>{T.emitLitDebugLogEvents&&T.dispatchEvent(new CustomEvent("lit-debug",{detail:r}))};let Vn=0,Ve;(tt=T.litIssuedWarnings)!==null&&tt!==void 0||(T.litIssuedWarnings=new Set),Ve=(r,e)=>{e+=r?` See https://lit.dev/msg/${r} for more information.`:"",T.litIssuedWarnings.has(e)||(console.warn(e),T.litIssuedWarnings.add(e))},Ve("dev-mode","Lit is in dev mode. Not recommended for production!");const R=!((rt=T.ShadyDOM)===null||rt===void 0)&&rt.inUse&&((nt=T.ShadyDOM)===null||nt===void 0?void 0:nt.noPatch)===!0?T.ShadyDOM.wrap:r=>r,se=T.trustedTypes,lr=se?se.createPolicy("lit-html",{createHTML:r=>r}):void 0,Dn=r=>r,Ue=(r,e,t)=>Dn,Fn=r=>{if(Y!==Ue)throw new Error("Attempted to overwrite existing lit-html security policy. setSanitizeDOMValueFactory should be called at most once.");Y=r},Un=()=>{Y=Ue},bt=(r,e,t)=>Y(r,e,t),wt="$lit$",F=`lit$${String(Math.random()).slice(9)}$`,Or="?"+F,jn=`<${Or}>`,J=document,fe=()=>J.createComment(""),ge=r=>r===null||typeof r!="object"&&typeof r!="function",Ar=Array.isArray,Bn=r=>Ar(r)||typeof(r==null?void 0:r[Symbol.iterator])=="function",it=`[ 	
\f\r]`,Wn=`[^ 	
\f\r"'\`<>=]`,Hn=`[^\\s"'>=/]`,de=/<(?:(!--|\/[^a-zA-Z])|(\/?[a-zA-Z][^>\s]*)|(\/?$))/g,cr=1,st=2,qn=3,dr=/-->/g,ur=/>/g,G=new RegExp(`>|${it}(?:(${Hn}+)(${it}*=${it}*(?:${Wn}|("|')|))|$)`,"g"),Gn=0,mr=1,Kn=2,pr=3,at=/'/g,lt=/"/g,Lr=/^(?:script|style|textarea|title)$/i,Qn=1,De=2,zt=1,Fe=2,Jn=3,Xn=4,Yn=5,Rt=6,Zn=7,Ir=r=>(e,...t)=>(e.some(n=>n===void 0)&&console.warn(`Some template strings are undefined.
This is probably caused by illegal octal escape sequences.`),{_$litType$:r,strings:e,values:t}),eo=Ir(Qn),ni=Ir(De),X=Symbol.for("lit-noChange"),b=Symbol.for("lit-nothing"),hr=new WeakMap,K=J.createTreeWalker(J,129,null,!1);let Y=Ue;function Mr(r,e){if(!Array.isArray(r)||!r.hasOwnProperty("raw")){let t="invalid template strings array";throw t=`
          Internal Error: expected template strings to be an array
          with a 'raw' field. Faking a template strings array by
          calling html or svg like an ordinary function is effectively
          the same as calling unsafeHtml and can lead to major security
          issues, e.g. opening your code up to XSS attacks.
          If you're using the html or svg tagged template functions normally
          and still seeing this error, please file a bug at
          https://github.com/lit/lit/issues/new?template=bug_report.md
          and include information about your build tooling, if any.
        `.trim().replace(/\n */g,`
`),new Error(t)}return lr!==void 0?lr.createHTML(e):e}const to=(r,e)=>{const t=r.length-1,n=[];let i=e===De?"<svg>":"",o,s=de;for(let a=0;a<t;a++){const l=r[a];let m=-1,h,u=0,f;for(;u<l.length&&(s.lastIndex=u,f=s.exec(l),f!==null);)if(u=s.lastIndex,s===de){if(f[cr]==="!--")s=dr;else if(f[cr]!==void 0)s=ur;else if(f[st]!==void 0)Lr.test(f[st])&&(o=new RegExp(`</${f[st]}`,"g")),s=G;else if(f[qn]!==void 0)throw new Error("Bindings in tag names are not supported. Please use static templates instead. See https://lit.dev/docs/templates/expressions/#static-expressions")}else s===G?f[Gn]===">"?(s=o??de,m=-1):f[mr]===void 0?m=-2:(m=s.lastIndex-f[Kn].length,h=f[mr],s=f[pr]===void 0?G:f[pr]==='"'?lt:at):s===lt||s===at?s=G:s===dr||s===ur?s=de:(s=G,o=void 0);console.assert(m===-1||s===G||s===at||s===lt,"unexpected parse state B");const U=s===G&&r[a+1].startsWith("/>")?" ":"";i+=s===de?l+jn:m>=0?(n.push(h),l.slice(0,m)+wt+l.slice(m)+F+U):l+F+(m===-2?(n.push(void 0),a):U)}const c=i+(r[t]||"<?>")+(e===De?"</svg>":"");return[Mr(r,c),n]};class ve{constructor({strings:e,["_$litType$"]:t},n){this.parts=[];let i,o=0,s=0;const c=e.length-1,a=this.parts,[l,m]=to(e,t);if(this.el=ve.createElement(l,n),K.currentNode=this.el.content,t===De){const h=this.el.content,u=h.firstChild;u.remove(),h.append(...u.childNodes)}for(;(i=K.nextNode())!==null&&a.length<c;){if(i.nodeType===1){{const h=i.localName;if(/^(?:textarea|template)$/i.test(h)&&i.innerHTML.includes(F)){const u=`Expressions are not supported inside \`${h}\` elements. See https://lit.dev/msg/expression-in-${h} for more information.`;if(h==="template")throw new Error(u);Ve("",u)}}if(i.hasAttributes()){const h=[];for(const u of i.getAttributeNames())if(u.endsWith(wt)||u.startsWith(F)){const f=m[s++];if(h.push(u),f!==void 0){const j=i.getAttribute(f.toLowerCase()+wt).split(F),V=/([.?@])?(.*)/.exec(f);a.push({type:zt,index:o,name:V[2],strings:j,ctor:V[1]==="."?no:V[1]==="?"?io:V[1]==="@"?so:je})}else a.push({type:Rt,index:o})}for(const u of h)i.removeAttribute(u)}if(Lr.test(i.tagName)){const h=i.textContent.split(F),u=h.length-1;if(u>0){i.textContent=se?se.emptyScript:"";for(let f=0;f<u;f++)i.append(h[f],fe()),K.nextNode(),a.push({type:Fe,index:++o});i.append(h[u],fe())}}}else if(i.nodeType===8)if(i.data===Or)a.push({type:Fe,index:o});else{let u=-1;for(;(u=i.data.indexOf(F,u+1))!==-1;)a.push({type:Zn,index:o}),u+=F.length-1}o++}g==null||g({kind:"template prep",template:this,clonableTemplate:this.el,parts:this.parts,strings:e})}static createElement(e,t){const n=J.createElement("template");return n.innerHTML=e,n}}function ae(r,e,t=r,n){var i,o,s,c;if(e===X)return e;let a=n!==void 0?(i=t.__directives)===null||i===void 0?void 0:i[n]:t.__directive;const l=ge(e)?void 0:e._$litDirective$;return(a==null?void 0:a.constructor)!==l&&((o=a==null?void 0:a._$notifyDirectiveConnectionChanged)===null||o===void 0||o.call(a,!1),l===void 0?a=void 0:(a=new l(r),a._$initialize(r,t,n)),n!==void 0?((s=(c=t).__directives)!==null&&s!==void 0?s:c.__directives=[])[n]=a:t.__directive=a),a!==void 0&&(e=ae(r,a._$resolve(r,e.values),a,n)),e}class ro{constructor(e,t){this._$parts=[],this._$disconnectableChildren=void 0,this._$template=e,this._$parent=t}get parentNode(){return this._$parent.parentNode}get _$isConnected(){return this._$parent._$isConnected}_clone(e){var t;const{el:{content:n},parts:i}=this._$template,o=((t=e==null?void 0:e.creationScope)!==null&&t!==void 0?t:J).importNode(n,!0);K.currentNode=o;let s=K.nextNode(),c=0,a=0,l=i[0];for(;l!==void 0;){if(c===l.index){let m;l.type===Fe?m=new ye(s,s.nextSibling,this,e):l.type===zt?m=new l.ctor(s,l.name,l.strings,this,e):l.type===Rt&&(m=new ao(s,this,e)),this._$parts.push(m),l=i[++a]}c!==(l==null?void 0:l.index)&&(s=K.nextNode(),c++)}return K.currentNode=J,o}_update(e){let t=0;for(const n of this._$parts)n!==void 0&&(g==null||g({kind:"set part",part:n,value:e[t],valueIndex:t,values:e,templateInstance:this}),n.strings!==void 0?(n._$setValue(e,n,t),t+=n.strings.length-2):n._$setValue(e[t])),t++}}class ye{constructor(e,t,n,i){var o;this.type=Fe,this._$committedValue=b,this._$disconnectableChildren=void 0,this._$startNode=e,this._$endNode=t,this._$parent=n,this.options=i,this.__isConnected=(o=i==null?void 0:i.isConnected)!==null&&o!==void 0?o:!0,this._textSanitizer=void 0}get _$isConnected(){var e,t;return(t=(e=this._$parent)===null||e===void 0?void 0:e._$isConnected)!==null&&t!==void 0?t:this.__isConnected}get parentNode(){let e=R(this._$startNode).parentNode;const t=this._$parent;return t!==void 0&&(e==null?void 0:e.nodeType)===11&&(e=t.parentNode),e}get startNode(){return this._$startNode}get endNode(){return this._$endNode}_$setValue(e,t=this){var n;if(this.parentNode===null)throw new Error("This `ChildPart` has no `parentNode` and therefore cannot accept a value. This likely means the element containing the part was manipulated in an unsupported way outside of Lit's control such that the part's marker nodes were ejected from DOM. For example, setting the element's `innerHTML` or `textContent` can do this.");if(e=ae(this,e,t),ge(e))e===b||e==null||e===""?(this._$committedValue!==b&&(g==null||g({kind:"commit nothing to child",start:this._$startNode,end:this._$endNode,parent:this._$parent,options:this.options}),this._$clear()),this._$committedValue=b):e!==this._$committedValue&&e!==X&&this._commitText(e);else if(e._$litType$!==void 0)this._commitTemplateResult(e);else if(e.nodeType!==void 0){if(((n=this.options)===null||n===void 0?void 0:n.host)===e){this._commitText("[probable mistake: rendered a template's host in itself (commonly caused by writing ${this} in a template]"),console.warn("Attempted to render the template host",e,"inside itself. This is almost always a mistake, and in dev mode ","we render some warning text. In production however, we'll ","render it, which will usually result in an error, and sometimes ","in the element disappearing from the DOM.");return}this._commitNode(e)}else Bn(e)?this._commitIterable(e):this._commitText(e)}_insert(e){return R(R(this._$startNode).parentNode).insertBefore(e,this._$endNode)}_commitNode(e){var t;if(this._$committedValue!==e){if(this._$clear(),Y!==Ue){const n=(t=this._$startNode.parentNode)===null||t===void 0?void 0:t.nodeName;if(n==="STYLE"||n==="SCRIPT"){let i="Forbidden";throw n==="STYLE"?i="Lit does not support binding inside style nodes. This is a security risk, as style injection attacks can exfiltrate data and spoof UIs. Consider instead using css`...` literals to compose styles, and make do dynamic styling with css custom properties, ::parts, <slot>s, and by mutating the DOM rather than stylesheets.":i="Lit does not support binding inside script nodes. This is a security risk, as it could allow arbitrary code execution.",new Error(i)}}g==null||g({kind:"commit node",start:this._$startNode,parent:this._$parent,value:e,options:this.options}),this._$committedValue=this._insert(e)}}_commitText(e){if(this._$committedValue!==b&&ge(this._$committedValue)){const t=R(this._$startNode).nextSibling;this._textSanitizer===void 0&&(this._textSanitizer=bt(t,"data","property")),e=this._textSanitizer(e),g==null||g({kind:"commit text",node:t,value:e,options:this.options}),t.data=e}else{const t=J.createTextNode("");this._commitNode(t),this._textSanitizer===void 0&&(this._textSanitizer=bt(t,"data","property")),e=this._textSanitizer(e),g==null||g({kind:"commit text",node:t,value:e,options:this.options}),t.data=e}this._$committedValue=e}_commitTemplateResult(e){var t;const{values:n,["_$litType$"]:i}=e,o=typeof i=="number"?this._$getTemplate(e):(i.el===void 0&&(i.el=ve.createElement(Mr(i.h,i.h[0]),this.options)),i);if(((t=this._$committedValue)===null||t===void 0?void 0:t._$template)===o)g==null||g({kind:"template updating",template:o,instance:this._$committedValue,parts:this._$committedValue._$parts,options:this.options,values:n}),this._$committedValue._update(n);else{const s=new ro(o,this),c=s._clone(this.options);g==null||g({kind:"template instantiated",template:o,instance:s,parts:s._$parts,options:this.options,fragment:c,values:n}),s._update(n),g==null||g({kind:"template instantiated and updated",template:o,instance:s,parts:s._$parts,options:this.options,fragment:c,values:n}),this._commitNode(c),this._$committedValue=s}}_$getTemplate(e){let t=hr.get(e.strings);return t===void 0&&hr.set(e.strings,t=new ve(e)),t}_commitIterable(e){Ar(this._$committedValue)||(this._$committedValue=[],this._$clear());const t=this._$committedValue;let n=0,i;for(const o of e)n===t.length?t.push(i=new ye(this._insert(fe()),this._insert(fe()),this,this.options)):i=t[n],i._$setValue(o),n++;n<t.length&&(this._$clear(i&&R(i._$endNode).nextSibling,n),t.length=n)}_$clear(e=R(this._$startNode).nextSibling,t){var n;for((n=this._$notifyConnectionChanged)===null||n===void 0||n.call(this,!1,!0,t);e&&e!==this._$endNode;){const i=R(e).nextSibling;R(e).remove(),e=i}}setConnected(e){var t;if(this._$parent===void 0)this.__isConnected=e,(t=this._$notifyConnectionChanged)===null||t===void 0||t.call(this,e);else throw new Error("part.setConnected() may only be called on a RootPart returned from render().")}}class je{constructor(e,t,n,i,o){this.type=zt,this._$committedValue=b,this._$disconnectableChildren=void 0,this.element=e,this.name=t,this._$parent=i,this.options=o,n.length>2||n[0]!==""||n[1]!==""?(this._$committedValue=new Array(n.length-1).fill(new String),this.strings=n):this._$committedValue=b,this._sanitizer=void 0}get tagName(){return this.element.tagName}get _$isConnected(){return this._$parent._$isConnected}_$setValue(e,t=this,n,i){const o=this.strings;let s=!1;if(o===void 0)e=ae(this,e,t,0),s=!ge(e)||e!==this._$committedValue&&e!==X,s&&(this._$committedValue=e);else{const c=e;e=o[0];let a,l;for(a=0;a<o.length-1;a++)l=ae(this,c[n+a],t,a),l===X&&(l=this._$committedValue[a]),s||(s=!ge(l)||l!==this._$committedValue[a]),l===b?e=b:e!==b&&(e+=(l??"")+o[a+1]),this._$committedValue[a]=l}s&&!i&&this._commitValue(e)}_commitValue(e){e===b?R(this.element).removeAttribute(this.name):(this._sanitizer===void 0&&(this._sanitizer=Y(this.element,this.name,"attribute")),e=this._sanitizer(e??""),g==null||g({kind:"commit attribute",element:this.element,name:this.name,value:e,options:this.options}),R(this.element).setAttribute(this.name,e??""))}}class no extends je{constructor(){super(...arguments),this.type=Jn}_commitValue(e){this._sanitizer===void 0&&(this._sanitizer=Y(this.element,this.name,"property")),e=this._sanitizer(e),g==null||g({kind:"commit property",element:this.element,name:this.name,value:e,options:this.options}),this.element[this.name]=e===b?void 0:e}}const oo=se?se.emptyScript:"";class io extends je{constructor(){super(...arguments),this.type=Xn}_commitValue(e){g==null||g({kind:"commit boolean attribute",element:this.element,name:this.name,value:!!(e&&e!==b),options:this.options}),e&&e!==b?R(this.element).setAttribute(this.name,oo):R(this.element).removeAttribute(this.name)}}class so extends je{constructor(e,t,n,i,o){if(super(e,t,n,i,o),this.type=Yn,this.strings!==void 0)throw new Error(`A \`<${e.localName}>\` has a \`@${t}=...\` listener with invalid content. Event listeners in templates must have exactly one expression and no surrounding text.`)}_$setValue(e,t=this){var n;if(e=(n=ae(this,e,t,0))!==null&&n!==void 0?n:b,e===X)return;const i=this._$committedValue,o=e===b&&i!==b||e.capture!==i.capture||e.once!==i.once||e.passive!==i.passive,s=e!==b&&(i===b||o);g==null||g({kind:"commit event listener",element:this.element,name:this.name,value:e,options:this.options,removeListener:o,addListener:s,oldListener:i}),o&&this.element.removeEventListener(this.name,this,i),s&&this.element.addEventListener(this.name,this,e),this._$committedValue=e}handleEvent(e){var t,n;typeof this._$committedValue=="function"?this._$committedValue.call((n=(t=this.options)===null||t===void 0?void 0:t.host)!==null&&n!==void 0?n:this.element,e):this._$committedValue.handleEvent(e)}}class ao{constructor(e,t,n){this.element=e,this.type=Rt,this._$disconnectableChildren=void 0,this._$parent=t,this.options=n}get _$isConnected(){return this._$parent._$isConnected}_$setValue(e){g==null||g({kind:"commit to element binding",element:this.element,value:e,options:this.options}),ae(this,e)}}const ct=T.litHtmlPolyfillSupportDevMode;ct==null||ct(ve,ye);((ot=T.litHtmlVersions)!==null&&ot!==void 0?ot:T.litHtmlVersions=[]).push("2.8.0");T.litHtmlVersions.length>1&&Ve("multiple-versions","Multiple versions of Lit loaded. Loading multiple versions is not recommended.");const Oe=(r,e,t)=>{var n,i;if(e==null)throw new TypeError(`The container to render into may not be ${e}`);const o=Vn++,s=(n=t==null?void 0:t.renderBefore)!==null&&n!==void 0?n:e;let c=s._$litPart$;if(g==null||g({kind:"begin render",id:o,value:r,container:e,options:t,part:c}),c===void 0){const a=(i=t==null?void 0:t.renderBefore)!==null&&i!==void 0?i:null;s._$litPart$=c=new ye(e.insertBefore(fe(),a),a,void 0,t??{})}return c._$setValue(r),g==null||g({kind:"end render",id:o,value:r,container:e,options:t,part:c}),c};Oe.setSanitizer=Fn,Oe.createSanitizer=bt,Oe._testOnlyClearSanitizerFactoryDoNotCallOrElse=Un;/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var dt,ut,mt;let Nt;{const r=(dt=globalThis.litIssuedWarnings)!==null&&dt!==void 0?dt:globalThis.litIssuedWarnings=new Set;Nt=(e,t)=>{t+=` See https://lit.dev/msg/${e} for more information.`,r.has(t)||(console.warn(t),r.add(t))}}class ce extends M{constructor(){super(...arguments),this.renderOptions={host:this},this.__childPart=void 0}createRenderRoot(){var e,t;const n=super.createRenderRoot();return(e=(t=this.renderOptions).renderBefore)!==null&&e!==void 0||(t.renderBefore=n.firstChild),n}update(e){const t=this.render();this.hasUpdated||(this.renderOptions.isConnected=this.isConnected),super.update(e),this.__childPart=Oe(t,this.renderRoot,this.renderOptions)}connectedCallback(){var e;super.connectedCallback(),(e=this.__childPart)===null||e===void 0||e.setConnected(!0)}disconnectedCallback(){var e;super.disconnectedCallback(),(e=this.__childPart)===null||e===void 0||e.setConnected(!1)}render(){return X}}ce.finalized=!0;ce._$litElement$=!0;(ut=globalThis.litElementHydrateSupport)===null||ut===void 0||ut.call(globalThis,{LitElement:ce});const pt=globalThis.litElementPolyfillSupportDevMode;pt==null||pt({LitElement:ce});ce.finalize=function(){if(!M.finalize.call(this))return!1;const e=(t,n,i=!1)=>{if(t.hasOwnProperty(n)){const o=(typeof t=="function"?t:t.constructor).name;Nt(i?"renamed-api":"removed-api",`\`${n}\` is implemented on class ${o}. It has been ${i?"renamed":"removed"} in this version of LitElement.`)}};return e(this,"render"),e(this,"getStyles",!0),e(this.prototype,"adoptStyles"),!0};((mt=globalThis.litElementVersions)!==null&&mt!==void 0?mt:globalThis.litElementVersions=[]).push("3.3.3");globalThis.litElementVersions.length>1&&Nt("multiple-versions","Multiple versions of Lit loaded. Loading multiple versions is not recommended.");/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const lo=(r,e)=>e.kind==="method"&&e.descriptor&&!("value"in e.descriptor)?{...e,finisher(t){t.createProperty(e.key,r)}}:{kind:"field",key:Symbol(),placement:"own",descriptor:{},originalKey:e.key,initializer(){typeof e.initializer=="function"&&(this[e.key]=e.initializer.call(this))},finisher(t){t.createProperty(e.key,r)}},co=(r,e,t)=>{e.constructor.createProperty(t,r)};function P(r){return(e,t)=>t!==void 0?co(r,e,t):lo(r,e)}/**
 * @license
 * Copyright 2021 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var ht;const uo=window;((ht=uo.HTMLSlotElement)===null||ht===void 0?void 0:ht.prototype.assignedElements)!=null;/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const mo={ATTRIBUTE:1,CHILD:2,PROPERTY:3,BOOLEAN_ATTRIBUTE:4,EVENT:5,ELEMENT:6},po=r=>(...e)=>({_$litDirective$:r,values:e});class ho{constructor(e){}get _$isConnected(){return this._$parent._$isConnected}_$initialize(e,t,n){this.__part=e,this._$parent=t,this.__attributeIndex=n}_$resolve(e,t){return this.update(e,t)}update(e,t){return this.render(...t)}}/**
 * @license
 * Copyright 2018 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */class fo extends ho{constructor(e){var t;if(super(e),e.type!==mo.ATTRIBUTE||e.name!=="class"||((t=e.strings)===null||t===void 0?void 0:t.length)>2)throw new Error("`classMap()` can only be used in the `class` attribute and must be the only part in the attribute.")}render(e){return" "+Object.keys(e).filter(t=>e[t]).join(" ")+" "}update(e,[t]){var n,i;if(this._previousClasses===void 0){this._previousClasses=new Set,e.strings!==void 0&&(this._staticClasses=new Set(e.strings.join(" ").split(/\s/).filter(s=>s!=="")));for(const s in t)t[s]&&!(!((n=this._staticClasses)===null||n===void 0)&&n.has(s))&&this._previousClasses.add(s);return this.render(t)}const o=e.element.classList;this._previousClasses.forEach(s=>{s in t||(o.remove(s),this._previousClasses.delete(s))});for(const s in t){const c=!!t[s];c!==this._previousClasses.has(s)&&!(!((i=this._staticClasses)===null||i===void 0)&&i.has(s))&&(c?(o.add(s),this._previousClasses.add(s)):(o.remove(s),this._previousClasses.delete(s)))}return X}}const go=po(fo),ft="css-loading-indicator";var k;(function(r){r.IDLE="",r.FIRST="first",r.SECOND="second",r.THIRD="third"})(k||(k={}));class w extends ce{constructor(){super(),this.firstDelay=450,this.secondDelay=1500,this.thirdDelay=5e3,this.expandedDuration=2e3,this.onlineText="Online",this.offlineText="Connection lost",this.reconnectingText="Connection lost, trying to reconnect...",this.offline=!1,this.reconnecting=!1,this.expanded=!1,this.loading=!1,this.loadingBarState=k.IDLE,this.applyDefaultThemeState=!0,this.firstTimeout=0,this.secondTimeout=0,this.thirdTimeout=0,this.expandedTimeout=0,this.lastMessageState=_.CONNECTED,this.connectionStateListener=()=>{this.expanded=this.updateConnectionState(),this.expandedTimeout=this.timeoutFor(this.expandedTimeout,this.expanded,()=>{this.expanded=!1},this.expandedDuration)}}static create(){var e,t;const n=window;return!((e=n.Vaadin)===null||e===void 0)&&e.connectionIndicator||(n.Vaadin=n.Vaadin||{},n.Vaadin.connectionIndicator=document.createElement("vaadin-connection-indicator"),document.body.appendChild(n.Vaadin.connectionIndicator)),(t=n.Vaadin)===null||t===void 0?void 0:t.connectionIndicator}render(){return eo`
      <div class="v-loading-indicator ${this.loadingBarState}" style=${this.getLoadingBarStyle()}></div>

      <div
        class="v-status-message ${go({active:this.reconnecting})}"
      >
        <span class="text"> ${this.renderMessage()} </span>
      </div>
    `}connectedCallback(){var e;super.connectedCallback();const t=window;!((e=t.Vaadin)===null||e===void 0)&&e.connectionState&&(this.connectionStateStore=t.Vaadin.connectionState,this.connectionStateStore.addStateChangeListener(this.connectionStateListener),this.updateConnectionState()),this.updateTheme()}disconnectedCallback(){super.disconnectedCallback(),this.connectionStateStore&&this.connectionStateStore.removeStateChangeListener(this.connectionStateListener),this.updateTheme()}get applyDefaultTheme(){return this.applyDefaultThemeState}set applyDefaultTheme(e){e!==this.applyDefaultThemeState&&(this.applyDefaultThemeState=e,this.updateTheme())}createRenderRoot(){return this}updateConnectionState(){var e;const t=(e=this.connectionStateStore)===null||e===void 0?void 0:e.state;return this.offline=t===_.CONNECTION_LOST,this.reconnecting=t===_.RECONNECTING,this.updateLoading(t===_.LOADING),this.loading?!1:t!==this.lastMessageState?(this.lastMessageState=t,!0):!1}updateLoading(e){this.loading=e,this.loadingBarState=k.IDLE,this.firstTimeout=this.timeoutFor(this.firstTimeout,e,()=>{this.loadingBarState=k.FIRST},this.firstDelay),this.secondTimeout=this.timeoutFor(this.secondTimeout,e,()=>{this.loadingBarState=k.SECOND},this.secondDelay),this.thirdTimeout=this.timeoutFor(this.thirdTimeout,e,()=>{this.loadingBarState=k.THIRD},this.thirdDelay)}renderMessage(){return this.reconnecting?this.reconnectingText:this.offline?this.offlineText:this.onlineText}updateTheme(){if(this.applyDefaultThemeState&&this.isConnected){if(!document.getElementById(ft)){const e=document.createElement("style");e.id=ft,e.textContent=this.getDefaultStyle(),document.head.appendChild(e)}}else{const e=document.getElementById(ft);e&&document.head.removeChild(e)}}getDefaultStyle(){return`
      @keyframes v-progress-start {
        0% {
          width: 0%;
        }
        100% {
          width: 50%;
        }
      }
      @keyframes v-progress-delay {
        0% {
          width: 50%;
        }
        100% {
          width: 90%;
        }
      }
      @keyframes v-progress-wait {
        0% {
          width: 90%;
          height: 4px;
        }
        3% {
          width: 91%;
          height: 7px;
        }
        100% {
          width: 96%;
          height: 7px;
        }
      }
      @keyframes v-progress-wait-pulse {
        0% {
          opacity: 1;
        }
        50% {
          opacity: 0.1;
        }
        100% {
          opacity: 1;
        }
      }
      .v-loading-indicator,
      .v-status-message {
        position: fixed;
        z-index: 251;
        left: 0;
        right: auto;
        top: 0;
        background-color: var(--lumo-primary-color, var(--material-primary-color, blue));
        transition: none;
      }
      .v-loading-indicator {
        width: 50%;
        height: 4px;
        opacity: 1;
        pointer-events: none;
        animation: v-progress-start 1000ms 200ms both;
      }
      .v-loading-indicator[style*='none'] {
        display: block !important;
        width: 100%;
        opacity: 0;
        animation: none;
        transition: opacity 500ms 300ms, width 300ms;
      }
      .v-loading-indicator.second {
        width: 90%;
        animation: v-progress-delay 3.8s forwards;
      }
      .v-loading-indicator.third {
        width: 96%;
        animation: v-progress-wait 5s forwards, v-progress-wait-pulse 1s 4s infinite backwards;
      }

      vaadin-connection-indicator[offline] .v-loading-indicator,
      vaadin-connection-indicator[reconnecting] .v-loading-indicator {
        display: none;
      }

      .v-status-message {
        opacity: 0;
        width: 100%;
        max-height: var(--status-height-collapsed, 8px);
        overflow: hidden;
        background-color: var(--status-bg-color-online, var(--lumo-primary-color, var(--material-primary-color, blue)));
        color: var(
          --status-text-color-online,
          var(--lumo-primary-contrast-color, var(--material-primary-contrast-color, #fff))
        );
        font-size: 0.75rem;
        font-weight: 600;
        line-height: 1;
        transition: all 0.5s;
        padding: 0 0.5em;
      }

      vaadin-connection-indicator[offline] .v-status-message,
      vaadin-connection-indicator[reconnecting] .v-status-message {
        opacity: 1;
        background-color: var(--status-bg-color-offline, var(--lumo-shade, #333));
        color: var(
          --status-text-color-offline,
          var(--lumo-primary-contrast-color, var(--material-primary-contrast-color, #fff))
        );
        background-image: repeating-linear-gradient(
          45deg,
          rgba(255, 255, 255, 0),
          rgba(255, 255, 255, 0) 10px,
          rgba(255, 255, 255, 0.1) 10px,
          rgba(255, 255, 255, 0.1) 20px
        );
      }

      vaadin-connection-indicator[reconnecting] .v-status-message {
        animation: show-reconnecting-status 2s;
      }

      vaadin-connection-indicator[offline] .v-status-message:hover,
      vaadin-connection-indicator[reconnecting] .v-status-message:hover,
      vaadin-connection-indicator[expanded] .v-status-message {
        max-height: var(--status-height, 1.75rem);
      }

      vaadin-connection-indicator[expanded] .v-status-message {
        opacity: 1;
      }

      .v-status-message span {
        display: flex;
        align-items: center;
        justify-content: center;
        height: var(--status-height, 1.75rem);
      }

      vaadin-connection-indicator[reconnecting] .v-status-message span::before {
        content: '';
        width: 1em;
        height: 1em;
        border-top: 2px solid
          var(--status-spinner-color, var(--lumo-primary-color, var(--material-primary-color, blue)));
        border-left: 2px solid
          var(--status-spinner-color, var(--lumo-primary-color, var(--material-primary-color, blue)));
        border-right: 2px solid transparent;
        border-bottom: 2px solid transparent;
        border-radius: 50%;
        box-sizing: border-box;
        animation: v-spin 0.4s linear infinite;
        margin: 0 0.5em;
      }

      @keyframes v-spin {
        100% {
          transform: rotate(360deg);
        }
      }
    `}getLoadingBarStyle(){switch(this.loadingBarState){case k.IDLE:return"display: none";case k.FIRST:case k.SECOND:case k.THIRD:return"display: block";default:return""}}timeoutFor(e,t,n,i){return e!==0&&window.clearTimeout(e),t?window.setTimeout(n,i):0}static get instance(){return w.create()}}$([P({type:Number})],w.prototype,"firstDelay",void 0);$([P({type:Number})],w.prototype,"secondDelay",void 0);$([P({type:Number})],w.prototype,"thirdDelay",void 0);$([P({type:Number})],w.prototype,"expandedDuration",void 0);$([P({type:String})],w.prototype,"onlineText",void 0);$([P({type:String})],w.prototype,"offlineText",void 0);$([P({type:String})],w.prototype,"reconnectingText",void 0);$([P({type:Boolean,reflect:!0})],w.prototype,"offline",void 0);$([P({type:Boolean,reflect:!0})],w.prototype,"reconnecting",void 0);$([P({type:Boolean,reflect:!0})],w.prototype,"expanded",void 0);$([P({type:Boolean,reflect:!0})],w.prototype,"loading",void 0);$([P({type:String})],w.prototype,"loadingBarState",void 0);$([P({type:Boolean})],w.prototype,"applyDefaultTheme",null);customElements.get("vaadin-connection-indicator")===void 0&&customElements.define("vaadin-connection-indicator",w);w.instance;const xe=window;xe.Vaadin=xe.Vaadin||{};xe.Vaadin.registrations=xe.Vaadin.registrations||[];xe.Vaadin.registrations.push({is:"@vaadin/common-frontend",version:"0.0.18"});class fr extends Error{}const ue=window.document.body,x=window;class vo{constructor(e){this.response=void 0,this.pathname="",this.isActive=!1,this.baseRegex=/^\//,this.navigation="",ue.$=ue.$||[],this.config=e||{},x.Vaadin=x.Vaadin||{},x.Vaadin.Flow=x.Vaadin.Flow||{},x.Vaadin.Flow.clients={TypeScript:{isActive:()=>this.isActive}};const t=document.head.querySelector("base");this.baseRegex=new RegExp(`^${(document.baseURI||t&&t.href||"/").replace(/^https?:\/\/[^/]+/i,"")}`),this.appShellTitle=document.title,this.addConnectionIndicator()}get serverSideRoutes(){return[{path:"(.*)",action:this.action}]}loadingStarted(){this.isActive=!0,x.Vaadin.connectionState.loadingStarted()}loadingFinished(){this.isActive=!1,x.Vaadin.connectionState.loadingFinished(),!x.Vaadin.listener&&(x.Vaadin.listener={},document.addEventListener("click",e=>{e.target&&(e.target.hasAttribute("router-link")?this.navigation="link":e.composedPath().some(t=>t.nodeName==="A")&&(this.navigation="client"))},{capture:!0}))}get action(){return async e=>{if(this.pathname=e.pathname,x.Vaadin.connectionState.online)try{await this.flowInit()}catch(t){if(t instanceof fr)return x.Vaadin.connectionState.state=_.CONNECTION_LOST,this.offlineStubAction();throw t}else return this.offlineStubAction();return this.container.onBeforeEnter=(t,n)=>this.flowNavigate(t,n),this.container.onBeforeLeave=(t,n)=>this.flowLeave(t,n),this.container}}async flowLeave(e,t){const{connectionState:n}=x.Vaadin;return this.pathname===e.pathname||!this.isFlowClientLoaded()||n.offline?Promise.resolve({}):new Promise(i=>{this.loadingStarted(),this.container.serverConnected=o=>{i(t&&o?t.prevent():{}),this.loadingFinished()},ue.$server.leaveNavigation(this.getFlowRoutePath(e),this.getFlowRouteQuery(e))})}async flowNavigate(e,t){return this.response?new Promise(n=>{this.loadingStarted(),this.container.serverConnected=(i,o)=>{t&&i?n(t.prevent()):t&&t.redirect&&o?n(t.redirect(o.pathname)):(this.container.style.display="",n(this.container)),this.loadingFinished()},this.container.serverPaused=()=>{this.loadingFinished()},ue.$server.connectClient(this.getFlowRoutePath(e),this.getFlowRouteQuery(e),this.appShellTitle,history.state,this.navigation),this.navigation="history"}):Promise.resolve(this.container)}getFlowRoutePath(e){return decodeURIComponent(e.pathname).replace(this.baseRegex,"")}getFlowRouteQuery(e){return e.search&&e.search.substring(1)||""}async flowInit(){if(!this.isFlowClientLoaded()){this.loadingStarted(),this.response=await this.flowInitUi();const{pushScript:e,appConfig:t}=this.response;typeof e=="string"&&await this.loadScript(e);const{appId:n}=t;await(await gt(()=>import("./FlowBootstrap-feff2646.js"),[],import.meta.url)).init(this.response),typeof this.config.imports=="function"&&(this.injectAppIdScript(n),await this.config.imports());const o=`flow-container-${n.toLowerCase()}`,s=document.querySelector(o);s?this.container=s:(this.container=document.createElement(o),this.container.id=n),ue.$[n]=this.container;const c=await gt(()=>import("./FlowClient-341d667e.js"),[],import.meta.url);await this.flowInitClient(c),this.loadingFinished()}return this.container&&!this.container.isConnected&&(this.container.style.display="none",document.body.appendChild(this.container)),this.response}async loadScript(e){return new Promise((t,n)=>{const i=document.createElement("script");i.onload=()=>t(),i.onerror=n,i.src=e,document.body.appendChild(i)})}injectAppIdScript(e){const t=e.substring(0,e.lastIndexOf("-")),n=document.createElement("script");n.type="module",n.setAttribute("data-app-id",t),document.body.append(n)}async flowInitClient(e){return e.init(),new Promise(t=>{const n=setInterval(()=>{Object.keys(x.Vaadin.Flow.clients).filter(o=>o!=="TypeScript").reduce((o,s)=>o||x.Vaadin.Flow.clients[s].isActive(),!1)||(clearInterval(n),t())},5)})}async flowInitUi(){const e=x.Vaadin&&x.Vaadin.TypeScript&&x.Vaadin.TypeScript.initial;return e?(x.Vaadin.TypeScript.initial=void 0,Promise.resolve(e)):new Promise((t,n)=>{const o=new XMLHttpRequest,s=`?v-r=init&location=${encodeURIComponent(this.getFlowRoutePath(location))}&query=${encodeURIComponent(this.getFlowRouteQuery(location))}`;o.open("GET",s),o.onerror=()=>n(new fr(`Invalid server response when initializing Flow UI.
        ${o.status}
        ${o.responseText}`)),o.onload=()=>{const c=o.getResponseHeader("content-type");c&&c.indexOf("application/json")!==-1?t(JSON.parse(o.responseText)):o.onerror()},o.send()})}addConnectionIndicator(){w.create(),x.addEventListener("online",()=>{if(!this.isFlowClientLoaded()){x.Vaadin.connectionState.state=_.RECONNECTING;const e=new XMLHttpRequest;e.open("HEAD","sw.js"),e.onload=()=>{x.Vaadin.connectionState.state=_.CONNECTED},e.onerror=()=>{x.Vaadin.connectionState.state=_.CONNECTION_LOST},setTimeout(()=>e.send(),50)}}),x.addEventListener("offline",()=>{this.isFlowClientLoaded()||(x.Vaadin.connectionState.state=_.CONNECTION_LOST)})}async offlineStubAction(){const e=document.createElement("iframe"),t="./offline-stub.html";e.setAttribute("src",t),e.setAttribute("style","width: 100%; height: 100%; border: 0"),this.response=void 0;let n;const i=()=>{n!==void 0&&(x.Vaadin.connectionState.removeStateChangeListener(n),n=void 0)};return e.onBeforeEnter=(o,s,c)=>{n=()=>{x.Vaadin.connectionState.online&&(i(),c.render(o,!1))},x.Vaadin.connectionState.addStateChangeListener(n)},e.onBeforeLeave=(o,s,c)=>{i()},e}isFlowClientLoaded(){return this.response!==void 0}}const{serverSideRoutes:xo}=new vo({imports:()=>gt(()=>import("./generated-flow-imports-87060759.js").then(r=>r.a3),[],import.meta.url)}),yo=[...xo],_o=new W(document.querySelector("#outlet"));_o.setRoutes(yo);(function(){if(typeof document>"u"||"adoptedStyleSheets"in document)return;var r="ShadyCSS"in window&&!ShadyCSS.nativeShadow,e=document.implementation.createHTMLDocument(""),t=new WeakMap,n=typeof DOMException=="object"?Error:DOMException,i=Object.defineProperty,o=Array.prototype.forEach,s=/@import.+?;?$/gm;function c(d){var p=d.replace(s,"");return p!==d&&console.warn("@import rules are not allowed here. See https://github.com/WICG/construct-stylesheets/issues/119#issuecomment-588352418"),p.trim()}function a(d){return"isConnected"in d?d.isConnected:document.contains(d)}function l(d){return d.filter(function(p,v){return d.indexOf(p)===v})}function m(d,p){return d.filter(function(v){return p.indexOf(v)===-1})}function h(d){d.parentNode.removeChild(d)}function u(d){return d.shadowRoot||t.get(d)}var f=["addRule","deleteRule","insertRule","removeRule"],U=CSSStyleSheet,j=U.prototype;j.replace=function(){return Promise.reject(new n("Can't call replace on non-constructed CSSStyleSheets."))},j.replaceSync=function(){throw new n("Failed to execute 'replaceSync' on 'CSSStyleSheet': Can't call replaceSync on non-constructed CSSStyleSheets.")};function V(d){return typeof d=="object"?te.isPrototypeOf(d)||j.isPrototypeOf(d):!1}function Be(d){return typeof d=="object"?j.isPrototypeOf(d):!1}var z=new WeakMap,L=new WeakMap,Z=new WeakMap,ee=new WeakMap;function We(d,p){var v=document.createElement("style");return Z.get(d).set(p,v),L.get(d).push(p),v}function D(d,p){return Z.get(d).get(p)}function be(d,p){Z.get(d).delete(p),L.set(d,L.get(d).filter(function(v){return v!==p}))}function It(d,p){requestAnimationFrame(function(){p.textContent=z.get(d).textContent,ee.get(d).forEach(function(v){return p.sheet[v.method].apply(p.sheet,v.args)})})}function we(d){if(!z.has(d))throw new TypeError("Illegal invocation")}function He(){var d=this,p=document.createElement("style");e.body.appendChild(p),z.set(d,p),L.set(d,[]),Z.set(d,new WeakMap),ee.set(d,[])}var te=He.prototype;te.replace=function(p){try{return this.replaceSync(p),Promise.resolve(this)}catch(v){return Promise.reject(v)}},te.replaceSync=function(p){if(we(this),typeof p=="string"){var v=this;z.get(v).textContent=c(p),ee.set(v,[]),L.get(v).forEach(function(E){E.isConnected()&&It(v,D(v,E))})}},i(te,"cssRules",{configurable:!0,enumerable:!0,get:function(){return we(this),z.get(this).sheet.cssRules}}),i(te,"media",{configurable:!0,enumerable:!0,get:function(){return we(this),z.get(this).sheet.media}}),f.forEach(function(d){te[d]=function(){var p=this;we(p);var v=arguments;ee.get(p).push({method:d,args:v}),L.get(p).forEach(function(C){if(C.isConnected()){var S=D(p,C).sheet;S[d].apply(S,v)}});var E=z.get(p).sheet;return E[d].apply(E,v)}}),i(He,Symbol.hasInstance,{configurable:!0,value:V});var Mt={childList:!0,subtree:!0},Vt=new WeakMap;function re(d){var p=Vt.get(d);return p||(p=new Ut(d),Vt.set(d,p)),p}function Dt(d){i(d.prototype,"adoptedStyleSheets",{configurable:!0,enumerable:!0,get:function(){return re(this).sheets},set:function(p){re(this).update(p)}})}function qe(d,p){for(var v=document.createNodeIterator(d,NodeFilter.SHOW_ELEMENT,function(C){return u(C)?NodeFilter.FILTER_ACCEPT:NodeFilter.FILTER_REJECT},null,!1),E=void 0;E=v.nextNode();)p(u(E))}var Se=new WeakMap,ne=new WeakMap,Ee=new WeakMap;function jr(d,p){return p instanceof HTMLStyleElement&&ne.get(d).some(function(v){return D(v,d)})}function Ft(d){var p=Se.get(d);return p instanceof Document?p.body:p}function Ge(d){var p=document.createDocumentFragment(),v=ne.get(d),E=Ee.get(d),C=Ft(d);E.disconnect(),v.forEach(function(S){p.appendChild(D(S,d)||We(S,d))}),C.insertBefore(p,null),E.observe(C,Mt),v.forEach(function(S){It(S,D(S,d))})}function Ut(d){var p=this;p.sheets=[],Se.set(p,d),ne.set(p,[]),Ee.set(p,new MutationObserver(function(v,E){if(!document){E.disconnect();return}v.forEach(function(C){r||o.call(C.addedNodes,function(S){S instanceof Element&&qe(S,function(oe){re(oe).connect()})}),o.call(C.removedNodes,function(S){S instanceof Element&&(jr(p,S)&&Ge(p),r||qe(S,function(oe){re(oe).disconnect()}))})})}))}if(Ut.prototype={isConnected:function(){var d=Se.get(this);return d instanceof Document?d.readyState!=="loading":a(d.host)},connect:function(){var d=Ft(this);Ee.get(this).observe(d,Mt),ne.get(this).length>0&&Ge(this),qe(d,function(p){re(p).connect()})},disconnect:function(){Ee.get(this).disconnect()},update:function(d){var p=this,v=Se.get(p)===document?"Document":"ShadowRoot";if(!Array.isArray(d))throw new TypeError("Failed to set the 'adoptedStyleSheets' property on "+v+": Iterator getter is not callable.");if(!d.every(V))throw new TypeError("Failed to set the 'adoptedStyleSheets' property on "+v+": Failed to convert value to 'CSSStyleSheet'");if(d.some(Be))throw new TypeError("Failed to set the 'adoptedStyleSheets' property on "+v+": Can't adopt non-constructed stylesheets");p.sheets=d;var E=ne.get(p),C=l(d),S=m(E,C);S.forEach(function(oe){h(D(oe,p)),be(oe,p)}),ne.set(p,C),p.isConnected()&&C.length>0&&Ge(p)}},window.CSSStyleSheet=He,Dt(Document),"ShadowRoot"in window){Dt(ShadowRoot);var jt=Element.prototype,Br=jt.attachShadow;jt.attachShadow=function(p){var v=Br.call(this,p);return p.mode==="closed"&&t.set(this,v),v}}var Ce=re(document);Ce.isConnected()?Ce.connect():document.addEventListener("DOMContentLoaded",Ce.connect.bind(Ce))})();const{toString:bo}=Object.prototype;function wo(r){return bo.call(r)==="[object RegExp]"}function So(r,{preserve:e=!0,whitespace:t=!0,all:n}={}){if(n)throw new Error("The `all` option is no longer supported. Use the `preserve` option instead.");let i=e,o;typeof e=="function"?(i=!1,o=e):wo(e)&&(i=!1,o=m=>e.test(m));let s=!1,c="",a="",l="";for(let m=0;m<r.length;m++){if(c=r[m],r[m-1]!=="\\"&&(c==='"'||c==="'")&&(s===c?s=!1:s||(s=c)),!s&&c==="/"&&r[m+1]==="*"){const h=r[m+2]==="!";let u=m+2;for(;u<r.length;u++){if(r[u]==="*"&&r[u+1]==="/"){i&&h||o&&o(a)?l+=`/*${a}*/`:t||(r[u+2]===`
`?u++:r[u+2]+r[u+3]===`\r
`&&(u+=2)),a="";break}a+=r[u]}m=u+1;continue}l+=c}return l}const Eo=CSSStyleSheet.toString().includes("document.createElement"),Co=(r,e)=>{const t=/(?:@media\s(.+?))?(?:\s{)?\@import\s*(?:url\(\s*['"]?(.+?)['"]?\s*\)|(["'])((?:\\.|[^\\])*?)\3)([^;]*);(?:})?/g;/\/\*(.|[\r\n])*?\*\//gm.exec(r)!=null&&(r=So(r));for(var n,i=r;(n=t.exec(r))!==null;){i=i.replace(n[0],"");const o=document.createElement("link");o.rel="stylesheet",o.href=n[2]||n[4];const s=n[1]||n[5];s&&(o.media=s),e===document?document.head.appendChild(o):e.appendChild(o)}return i},To=(r,e,t)=>(t?e.adoptedStyleSheets=[r,...e.adoptedStyleSheets]:e.adoptedStyleSheets=[...e.adoptedStyleSheets,r],()=>{e.adoptedStyleSheets=e.adoptedStyleSheets.filter(n=>n!==r)}),$o=(r,e,t)=>{const n=new CSSStyleSheet;return n.replaceSync(r),Eo?To(n,e,t):(t?e.adoptedStyleSheets.splice(0,0,n):e.adoptedStyleSheets.push(n),()=>{e.adoptedStyleSheets.splice(e.adoptedStyleSheets.indexOf(n),1)})},Po=(r,e)=>{const t=document.createElement("style");t.type="text/css",t.textContent=r;let n;if(e){const o=Array.from(document.head.childNodes).filter(s=>s.nodeType===Node.COMMENT_NODE).find(s=>s.data.trim()===e);o&&(n=o)}return document.head.insertBefore(t,n),()=>{t.remove()}},me=(r,e,t,n)=>{if(t===document){const o=zo(r);if(window.Vaadin.theme.injectedGlobalCss.indexOf(o)!==-1)return;window.Vaadin.theme.injectedGlobalCss.push(o)}const i=Co(r,t);return t===document?Po(i,e):$o(i,t,n)};window.Vaadin=window.Vaadin||{};window.Vaadin.theme=window.Vaadin.theme||{};window.Vaadin.theme.injectedGlobalCss=[];function gr(r){let e,t,n=2166136261;for(e=0,t=r.length;e<t;e++)n^=r.charCodeAt(e),n+=(n<<1)+(n<<4)+(n<<7)+(n<<8)+(n<<24);return("0000000"+(n>>>0).toString(16)).substr(-8)}function zo(r){let e=gr(r);return e+gr(e+r)}/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Ro=r=>class extends r{static get properties(){return{_theme:{type:String,readOnly:!0}}}static get observedAttributes(){return[...super.observedAttributes,"theme"]}attributeChangedCallback(t,n,i){super.attributeChangedCallback(t,n,i),t==="theme"&&this._set_theme(i)}};/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Vr=[];function Dr(r){return r&&Object.prototype.hasOwnProperty.call(r,"__themes")}function No(r){return Dr(customElements.get(r))}function ko(r=[]){return[r].flat(1/0).filter(e=>e instanceof $t?!0:(console.warn("An item in styles is not of type CSSResult. Use `unsafeCSS` or `css`."),!1))}function _e(r,e,t={}){r&&No(r)&&console.warn(`The custom element definition for "${r}"
      was finalized before a style module was registered.
      Make sure to add component specific style modules before
      importing the corresponding custom element.`),e=ko(e),window.Vaadin&&window.Vaadin.styleModules?window.Vaadin.styleModules.registerStyles(r,e,t):Vr.push({themeFor:r,styles:e,include:t.include,moduleId:t.moduleId})}function St(){return window.Vaadin&&window.Vaadin.styleModules?window.Vaadin.styleModules.getAllThemes():Vr}function Oo(r,e){return(r||"").split(" ").some(t=>new RegExp(`^${t.split("*").join(".*")}$`,"u").test(e))}function Ao(r=""){let e=0;return r.startsWith("lumo-")||r.startsWith("material-")?e=1:r.startsWith("vaadin-")&&(e=2),e}function Fr(r){const e=[];return r.include&&[].concat(r.include).forEach(t=>{const n=St().find(i=>i.moduleId===t);n?e.push(...Fr(n),...n.styles):console.warn(`Included moduleId ${t} not found in style registry`)},r.styles),e}function Lo(r,e){const t=document.createElement("style");t.innerHTML=r.map(n=>n.cssText).join(`
`),e.content.appendChild(t)}function Io(r){const e=`${r}-default-theme`,t=St().filter(n=>n.moduleId!==e&&Oo(n.themeFor,r)).map(n=>({...n,styles:[...Fr(n),...n.styles],includePriority:Ao(n.moduleId)})).sort((n,i)=>i.includePriority-n.includePriority);return t.length>0?t:St().filter(n=>n.moduleId===e)}const ii=r=>class extends Ro(r){static finalize(){if(super.finalize(),this.elementStyles)return;const t=this.prototype._template;!t||Dr(this)||Lo(this.getStylesForThis(),t)}static finalizeStyles(t){const n=this.getStylesForThis();return t?[...super.finalizeStyles(t),...n]:n}static getStylesForThis(){const t=Object.getPrototypeOf(this.prototype),n=(t?t.constructor.__themes:[])||[];this.__themes=[...n,...Io(this.is)];const i=this.__themes.flatMap(o=>o.styles);return i.filter((o,s)=>s===i.lastIndexOf(o))}};/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Mo=(r,...e)=>{const t=document.createElement("style");t.id=r,t.textContent=e.map(n=>n.toString()).join(`
`).replace(":host","html"),document.head.insertAdjacentElement("afterbegin",t)},Vo=y`.high-rating{background-color:var(--lumo-success-color-5pct)}.low-rating{background-color:var(--lumo-error-color-10pct)}.font-weight-bold{font-weight:700}
`;document._vaadintheme_megpbr_componentCss||(_e("vaadin-grid",Pt(Vo.toString())),document._vaadintheme_megpbr_componentCss=!0);/**
 * @license
 * Copyright (c) 2021 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */function Do(r){const e=customElements.get(r.is);if(!e)customElements.define(r.is,r);else{const t=e.version;t&&r.version&&t===r.version?console.warn(`The component ${r.is} has been loaded twice`):console.error(`Tried to define ${r.is} version ${r.version} when version ${e.version} is already in use. Something will probably break.`)}}/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */class Fo extends HTMLElement{static get is(){return"vaadin-lumo-styles"}static get version(){return"24.2.0"}}Do(Fo);const q=(r,...e)=>{Mo(`lumo-${r}`,e)};/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Uo=y`
  :host {
    /* prettier-ignore */
    --lumo-font-family: -apple-system, BlinkMacSystemFont, 'Roboto', 'Segoe UI', Helvetica, Arial, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol';

    /* Font sizes */
    --lumo-font-size-xxs: 0.75rem;
    --lumo-font-size-xs: 0.8125rem;
    --lumo-font-size-s: 0.875rem;
    --lumo-font-size-m: 1rem;
    --lumo-font-size-l: 1.125rem;
    --lumo-font-size-xl: 1.375rem;
    --lumo-font-size-xxl: 1.75rem;
    --lumo-font-size-xxxl: 2.5rem;

    /* Line heights */
    --lumo-line-height-xs: 1.25;
    --lumo-line-height-s: 1.375;
    --lumo-line-height-m: 1.625;
  }
`,kt=y`
  body,
  :host {
    font-family: var(--lumo-font-family);
    font-size: var(--lumo-font-size-m);
    line-height: var(--lumo-line-height-m);
    -webkit-text-size-adjust: 100%;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }

  small,
  [theme~='font-size-s'] {
    font-size: var(--lumo-font-size-s);
    line-height: var(--lumo-line-height-s);
  }

  [theme~='font-size-xs'] {
    font-size: var(--lumo-font-size-xs);
    line-height: var(--lumo-line-height-xs);
  }

  :where(h1, h2, h3, h4, h5, h6) {
    font-weight: 600;
    line-height: var(--lumo-line-height-xs);
    margin-block: 0;
  }

  :where(h1) {
    font-size: var(--lumo-font-size-xxxl);
  }

  :where(h2) {
    font-size: var(--lumo-font-size-xxl);
  }

  :where(h3) {
    font-size: var(--lumo-font-size-xl);
  }

  :where(h4) {
    font-size: var(--lumo-font-size-l);
  }

  :where(h5) {
    font-size: var(--lumo-font-size-m);
  }

  :where(h6) {
    font-size: var(--lumo-font-size-xs);
    text-transform: uppercase;
    letter-spacing: 0.03em;
  }

  p,
  blockquote {
    margin-top: 0.5em;
    margin-bottom: 0.75em;
  }

  a {
    text-decoration: none;
  }

  a:where(:any-link):hover {
    text-decoration: underline;
  }

  hr {
    display: block;
    align-self: stretch;
    height: 1px;
    border: 0;
    padding: 0;
    margin: var(--lumo-space-s) calc(var(--lumo-border-radius-m) / 2);
    background-color: var(--lumo-contrast-10pct);
  }

  blockquote {
    border-left: 2px solid var(--lumo-contrast-30pct);
  }

  b,
  strong {
    font-weight: 600;
  }

  /* RTL specific styles */
  blockquote[dir='rtl'] {
    border-left: none;
    border-right: 2px solid var(--lumo-contrast-30pct);
  }
`;_e("",kt,{moduleId:"lumo-typography"});q("typography-props",Uo);/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const jo=y`
  ${Pt(kt.cssText.replace(/,\s*:host/su,""))}
`;q("typography",jo,!1);/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Bo=y`
  :host {
    /* Base (background) */
    --lumo-base-color: #fff;

    /* Tint */
    --lumo-tint-5pct: hsla(0, 0%, 100%, 0.3);
    --lumo-tint-10pct: hsla(0, 0%, 100%, 0.37);
    --lumo-tint-20pct: hsla(0, 0%, 100%, 0.44);
    --lumo-tint-30pct: hsla(0, 0%, 100%, 0.5);
    --lumo-tint-40pct: hsla(0, 0%, 100%, 0.57);
    --lumo-tint-50pct: hsla(0, 0%, 100%, 0.64);
    --lumo-tint-60pct: hsla(0, 0%, 100%, 0.7);
    --lumo-tint-70pct: hsla(0, 0%, 100%, 0.77);
    --lumo-tint-80pct: hsla(0, 0%, 100%, 0.84);
    --lumo-tint-90pct: hsla(0, 0%, 100%, 0.9);
    --lumo-tint: #fff;

    /* Shade */
    --lumo-shade-5pct: hsla(214, 61%, 25%, 0.05);
    --lumo-shade-10pct: hsla(214, 57%, 24%, 0.1);
    --lumo-shade-20pct: hsla(214, 53%, 23%, 0.16);
    --lumo-shade-30pct: hsla(214, 50%, 22%, 0.26);
    --lumo-shade-40pct: hsla(214, 47%, 21%, 0.38);
    --lumo-shade-50pct: hsla(214, 45%, 20%, 0.52);
    --lumo-shade-60pct: hsla(214, 43%, 19%, 0.6);
    --lumo-shade-70pct: hsla(214, 42%, 18%, 0.69);
    --lumo-shade-80pct: hsla(214, 41%, 17%, 0.83);
    --lumo-shade-90pct: hsla(214, 40%, 16%, 0.94);
    --lumo-shade: hsl(214, 35%, 15%);

    /* Contrast */
    --lumo-contrast-5pct: var(--lumo-shade-5pct);
    --lumo-contrast-10pct: var(--lumo-shade-10pct);
    --lumo-contrast-20pct: var(--lumo-shade-20pct);
    --lumo-contrast-30pct: var(--lumo-shade-30pct);
    --lumo-contrast-40pct: var(--lumo-shade-40pct);
    --lumo-contrast-50pct: var(--lumo-shade-50pct);
    --lumo-contrast-60pct: var(--lumo-shade-60pct);
    --lumo-contrast-70pct: var(--lumo-shade-70pct);
    --lumo-contrast-80pct: var(--lumo-shade-80pct);
    --lumo-contrast-90pct: var(--lumo-shade-90pct);
    --lumo-contrast: var(--lumo-shade);

    /* Text */
    --lumo-header-text-color: var(--lumo-contrast);
    --lumo-body-text-color: var(--lumo-contrast-90pct);
    --lumo-secondary-text-color: var(--lumo-contrast-70pct);
    --lumo-tertiary-text-color: var(--lumo-contrast-50pct);
    --lumo-disabled-text-color: var(--lumo-contrast-30pct);

    /* Primary */
    --lumo-primary-color: hsl(214, 100%, 48%);
    --lumo-primary-color-50pct: hsla(214, 100%, 49%, 0.76);
    --lumo-primary-color-10pct: hsla(214, 100%, 60%, 0.13);
    --lumo-primary-text-color: hsl(214, 100%, 43%);
    --lumo-primary-contrast-color: #fff;

    /* Error */
    --lumo-error-color: hsl(3, 85%, 48%);
    --lumo-error-color-50pct: hsla(3, 85%, 49%, 0.5);
    --lumo-error-color-10pct: hsla(3, 85%, 49%, 0.1);
    --lumo-error-text-color: hsl(3, 89%, 42%);
    --lumo-error-contrast-color: #fff;

    /* Success */
    --lumo-success-color: hsl(145, 72%, 30%);
    --lumo-success-color-50pct: hsla(145, 72%, 31%, 0.5);
    --lumo-success-color-10pct: hsla(145, 72%, 31%, 0.1);
    --lumo-success-text-color: hsl(145, 85%, 25%);
    --lumo-success-contrast-color: #fff;

    /* Warning */
    --lumo-warning-color: hsl(48, 100%, 50%);
    --lumo-warning-color-10pct: hsla(48, 100%, 50%, 0.25);
    --lumo-warning-text-color: hsl(32, 100%, 30%);
    --lumo-warning-contrast-color: var(--lumo-shade-90pct);
  }

  /* forced-colors mode adjustments */
  @media (forced-colors: active) {
    html {
      --lumo-disabled-text-color: GrayText;
    }
  }
`;q("color-props",Bo);const Ot=y`
  [theme~='dark'] {
    /* Base (background) */
    --lumo-base-color: hsl(214, 35%, 21%);

    /* Tint */
    --lumo-tint-5pct: hsla(214, 65%, 85%, 0.06);
    --lumo-tint-10pct: hsla(214, 60%, 80%, 0.14);
    --lumo-tint-20pct: hsla(214, 64%, 82%, 0.23);
    --lumo-tint-30pct: hsla(214, 69%, 84%, 0.32);
    --lumo-tint-40pct: hsla(214, 73%, 86%, 0.41);
    --lumo-tint-50pct: hsla(214, 78%, 88%, 0.5);
    --lumo-tint-60pct: hsla(214, 82%, 90%, 0.58);
    --lumo-tint-70pct: hsla(214, 87%, 92%, 0.69);
    --lumo-tint-80pct: hsla(214, 91%, 94%, 0.8);
    --lumo-tint-90pct: hsla(214, 96%, 96%, 0.9);
    --lumo-tint: hsl(214, 100%, 98%);

    /* Shade */
    --lumo-shade-5pct: hsla(214, 0%, 0%, 0.07);
    --lumo-shade-10pct: hsla(214, 4%, 2%, 0.15);
    --lumo-shade-20pct: hsla(214, 8%, 4%, 0.23);
    --lumo-shade-30pct: hsla(214, 12%, 6%, 0.32);
    --lumo-shade-40pct: hsla(214, 16%, 8%, 0.41);
    --lumo-shade-50pct: hsla(214, 20%, 10%, 0.5);
    --lumo-shade-60pct: hsla(214, 24%, 12%, 0.6);
    --lumo-shade-70pct: hsla(214, 28%, 13%, 0.7);
    --lumo-shade-80pct: hsla(214, 32%, 13%, 0.8);
    --lumo-shade-90pct: hsla(214, 33%, 13%, 0.9);
    --lumo-shade: hsl(214, 33%, 13%);

    /* Contrast */
    --lumo-contrast-5pct: var(--lumo-tint-5pct);
    --lumo-contrast-10pct: var(--lumo-tint-10pct);
    --lumo-contrast-20pct: var(--lumo-tint-20pct);
    --lumo-contrast-30pct: var(--lumo-tint-30pct);
    --lumo-contrast-40pct: var(--lumo-tint-40pct);
    --lumo-contrast-50pct: var(--lumo-tint-50pct);
    --lumo-contrast-60pct: var(--lumo-tint-60pct);
    --lumo-contrast-70pct: var(--lumo-tint-70pct);
    --lumo-contrast-80pct: var(--lumo-tint-80pct);
    --lumo-contrast-90pct: var(--lumo-tint-90pct);
    --lumo-contrast: var(--lumo-tint);

    /* Text */
    --lumo-header-text-color: var(--lumo-contrast);
    --lumo-body-text-color: var(--lumo-contrast-90pct);
    --lumo-secondary-text-color: var(--lumo-contrast-70pct);
    --lumo-tertiary-text-color: var(--lumo-contrast-50pct);
    --lumo-disabled-text-color: var(--lumo-contrast-30pct);

    /* Primary */
    --lumo-primary-color: hsl(214, 90%, 48%);
    --lumo-primary-color-50pct: hsla(214, 90%, 70%, 0.69);
    --lumo-primary-color-10pct: hsla(214, 90%, 55%, 0.13);
    --lumo-primary-text-color: hsl(214, 90%, 77%);
    --lumo-primary-contrast-color: #fff;

    /* Error */
    --lumo-error-color: hsl(3, 79%, 49%);
    --lumo-error-color-50pct: hsla(3, 75%, 62%, 0.5);
    --lumo-error-color-10pct: hsla(3, 75%, 62%, 0.14);
    --lumo-error-text-color: hsl(3, 100%, 80%);

    /* Success */
    --lumo-success-color: hsl(145, 72%, 30%);
    --lumo-success-color-50pct: hsla(145, 92%, 51%, 0.5);
    --lumo-success-color-10pct: hsla(145, 92%, 51%, 0.1);
    --lumo-success-text-color: hsl(145, 85%, 46%);

    /* Warning */
    --lumo-warning-color: hsl(43, 100%, 48%);
    --lumo-warning-color-10pct: hsla(40, 100%, 50%, 0.2);
    --lumo-warning-text-color: hsl(45, 100%, 60%);
    --lumo-warning-contrast-color: var(--lumo-shade-90pct);
  }

  html {
    color: var(--lumo-body-text-color);
    background-color: var(--lumo-base-color);
    color-scheme: light;
  }

  [theme~='dark'] {
    color: var(--lumo-body-text-color);
    background-color: var(--lumo-base-color);
    color-scheme: dark;
  }

  h1,
  h2,
  h3,
  h4,
  h5,
  h6 {
    color: var(--lumo-header-text-color);
  }

  a:where(:any-link) {
    color: var(--lumo-primary-text-color);
  }

  a:not(:any-link) {
    color: var(--lumo-disabled-text-color);
  }

  blockquote {
    color: var(--lumo-secondary-text-color);
  }

  code,
  pre {
    background-color: var(--lumo-contrast-10pct);
    border-radius: var(--lumo-border-radius-m);
  }
`;_e("",Ot,{moduleId:"lumo-color"});/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */q("color",Ot);/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Ur=y`
  :host {
    /* Square */
    --lumo-space-xs: 0.25rem;
    --lumo-space-s: 0.5rem;
    --lumo-space-m: 1rem;
    --lumo-space-l: 1.5rem;
    --lumo-space-xl: 2.5rem;

    /* Wide */
    --lumo-space-wide-xs: calc(var(--lumo-space-xs) / 2) var(--lumo-space-xs);
    --lumo-space-wide-s: calc(var(--lumo-space-s) / 2) var(--lumo-space-s);
    --lumo-space-wide-m: calc(var(--lumo-space-m) / 2) var(--lumo-space-m);
    --lumo-space-wide-l: calc(var(--lumo-space-l) / 2) var(--lumo-space-l);
    --lumo-space-wide-xl: calc(var(--lumo-space-xl) / 2) var(--lumo-space-xl);

    /* Tall */
    --lumo-space-tall-xs: var(--lumo-space-xs) calc(var(--lumo-space-xs) / 2);
    --lumo-space-tall-s: var(--lumo-space-s) calc(var(--lumo-space-s) / 2);
    --lumo-space-tall-m: var(--lumo-space-m) calc(var(--lumo-space-m) / 2);
    --lumo-space-tall-l: var(--lumo-space-l) calc(var(--lumo-space-l) / 2);
    --lumo-space-tall-xl: var(--lumo-space-xl) calc(var(--lumo-space-xl) / 2);
  }
`;q("spacing-props",Ur);/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Wo=y`
  :host {
    /* Border radius */
    --lumo-border-radius-s: 0.25em; /* Checkbox, badge, date-picker year indicator, etc */
    --lumo-border-radius-m: var(--lumo-border-radius, 0.25em); /* Button, text field, menu overlay, etc */
    --lumo-border-radius-l: 0.5em; /* Dialog, notification, etc */

    /* Shadow */
    --lumo-box-shadow-xs: 0 1px 4px -1px var(--lumo-shade-50pct);
    --lumo-box-shadow-s: 0 2px 4px -1px var(--lumo-shade-20pct), 0 3px 12px -1px var(--lumo-shade-30pct);
    --lumo-box-shadow-m: 0 2px 6px -1px var(--lumo-shade-20pct), 0 8px 24px -4px var(--lumo-shade-40pct);
    --lumo-box-shadow-l: 0 3px 18px -2px var(--lumo-shade-20pct), 0 12px 48px -6px var(--lumo-shade-40pct);
    --lumo-box-shadow-xl: 0 4px 24px -3px var(--lumo-shade-20pct), 0 18px 64px -8px var(--lumo-shade-40pct);

    /* Clickable element cursor */
    --lumo-clickable-cursor: default;
  }
`;y`
  html {
    --vaadin-checkbox-size: calc(var(--lumo-size-m) / 2);
    --vaadin-radio-button-size: calc(var(--lumo-size-m) / 2);
    --vaadin-input-field-border-radius: var(--lumo-border-radius-m);
  }
`;q("style-props",Wo);/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const At=y`
  [theme~='badge'] {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
    padding: 0.4em calc(0.5em + var(--lumo-border-radius-s) / 4);
    color: var(--lumo-primary-text-color);
    background-color: var(--lumo-primary-color-10pct);
    border-radius: var(--lumo-border-radius-s);
    font-family: var(--lumo-font-family);
    font-size: var(--lumo-font-size-s);
    line-height: 1;
    font-weight: 500;
    text-transform: initial;
    letter-spacing: initial;
    min-width: calc(var(--lumo-line-height-xs) * 1em + 0.45em);
    flex-shrink: 0;
  }

  /* Ensure proper vertical alignment */
  [theme~='badge']::before {
    display: inline-block;
    content: '\\2003';
    width: 0;
  }

  [theme~='badge'][theme~='small'] {
    font-size: var(--lumo-font-size-xxs);
    line-height: 1;
  }

  /* Colors */

  [theme~='badge'][theme~='success'] {
    color: var(--lumo-success-text-color);
    background-color: var(--lumo-success-color-10pct);
  }

  [theme~='badge'][theme~='error'] {
    color: var(--lumo-error-text-color);
    background-color: var(--lumo-error-color-10pct);
  }

  [theme~='badge'][theme~='warning'] {
    color: var(--lumo-warning-text-color);
    background-color: var(--lumo-warning-color-10pct);
  }

  [theme~='badge'][theme~='contrast'] {
    color: var(--lumo-contrast-80pct);
    background-color: var(--lumo-contrast-5pct);
  }

  /* Primary */

  [theme~='badge'][theme~='primary'] {
    color: var(--lumo-primary-contrast-color);
    background-color: var(--lumo-primary-color);
  }

  [theme~='badge'][theme~='success'][theme~='primary'] {
    color: var(--lumo-success-contrast-color);
    background-color: var(--lumo-success-color);
  }

  [theme~='badge'][theme~='error'][theme~='primary'] {
    color: var(--lumo-error-contrast-color);
    background-color: var(--lumo-error-color);
  }

  [theme~='badge'][theme~='warning'][theme~='primary'] {
    color: var(--lumo-warning-contrast-color);
    background-color: var(--lumo-warning-color);
  }

  [theme~='badge'][theme~='contrast'][theme~='primary'] {
    color: var(--lumo-base-color);
    background-color: var(--lumo-contrast);
  }

  /* Links */

  [theme~='badge'][href]:hover {
    text-decoration: none;
  }

  /* Icon */

  [theme~='badge'] vaadin-icon {
    margin: -0.25em 0;
  }

  [theme~='badge'] vaadin-icon:first-child {
    margin-left: -0.375em;
  }

  [theme~='badge'] vaadin-icon:last-child {
    margin-right: -0.375em;
  }

  vaadin-icon[theme~='badge'][icon] {
    min-width: 0;
    padding: 0;
    font-size: 1rem;
    width: var(--lumo-icon-size-m);
    height: var(--lumo-icon-size-m);
  }

  vaadin-icon[theme~='badge'][icon][theme~='small'] {
    width: var(--lumo-icon-size-s);
    height: var(--lumo-icon-size-s);
  }

  /* Empty */

  [theme~='badge']:not([icon]):empty {
    min-width: 0;
    width: 1em;
    height: 1em;
    padding: 0;
    border-radius: 50%;
    background-color: var(--lumo-primary-color);
  }

  [theme~='badge'][theme~='small']:not([icon]):empty {
    width: 0.75em;
    height: 0.75em;
  }

  [theme~='badge'][theme~='contrast']:not([icon]):empty {
    background-color: var(--lumo-contrast);
  }

  [theme~='badge'][theme~='success']:not([icon]):empty {
    background-color: var(--lumo-success-color);
  }

  [theme~='badge'][theme~='error']:not([icon]):empty {
    background-color: var(--lumo-error-color);
  }

  [theme~='badge'][theme~='warning']:not([icon]):empty {
    background-color: var(--lumo-warning-color);
  }

  /* Pill */

  [theme~='badge'][theme~='pill'] {
    --lumo-border-radius-s: 1em;
  }

  /* RTL specific styles */

  [dir='rtl'][theme~='badge'] vaadin-icon:first-child {
    margin-right: -0.375em;
    margin-left: 0;
  }

  [dir='rtl'][theme~='badge'] vaadin-icon:last-child {
    margin-left: -0.375em;
    margin-right: 0;
  }
`;_e("",At,{moduleId:"lumo-badge"});/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */q("badge",At);/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Ho=y`
  /* === Screen readers === */
  .sr-only {
    border-width: 0;
    clip: rect(0, 0, 0, 0);
    height: 1px;
    margin: -1px;
    overflow: hidden;
    padding: 0;
    position: absolute;
    white-space: nowrap;
    width: 1px;
  }
`;/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const qo=y`
  /* === Background color === */
  .bg-base {
    background-color: var(--lumo-base-color);
  }

  .bg-transparent {
    background-color: transparent;
  }

  .bg-contrast-5 {
    background-color: var(--lumo-contrast-5pct);
  }
  .bg-contrast-10 {
    background-color: var(--lumo-contrast-10pct);
  }
  .bg-contrast-20 {
    background-color: var(--lumo-contrast-20pct);
  }
  .bg-contrast-30 {
    background-color: var(--lumo-contrast-30pct);
  }
  .bg-contrast-40 {
    background-color: var(--lumo-contrast-40pct);
  }
  .bg-contrast-50 {
    background-color: var(--lumo-contrast-50pct);
  }
  .bg-contrast-60 {
    background-color: var(--lumo-contrast-60pct);
  }
  .bg-contrast-70 {
    background-color: var(--lumo-contrast-70pct);
  }
  .bg-contrast-80 {
    background-color: var(--lumo-contrast-80pct);
  }
  .bg-contrast-90 {
    background-color: var(--lumo-contrast-90pct);
  }
  .bg-contrast {
    background-color: var(--lumo-contrast);
  }

  .bg-primary {
    background-color: var(--lumo-primary-color);
  }
  .bg-primary-50 {
    background-color: var(--lumo-primary-color-50pct);
  }
  .bg-primary-10 {
    background-color: var(--lumo-primary-color-10pct);
  }

  .bg-error {
    background-color: var(--lumo-error-color);
  }
  .bg-error-50 {
    background-color: var(--lumo-error-color-50pct);
  }
  .bg-error-10 {
    background-color: var(--lumo-error-color-10pct);
  }

  .bg-success {
    background-color: var(--lumo-success-color);
  }
  .bg-success-50 {
    background-color: var(--lumo-success-color-50pct);
  }
  .bg-success-10 {
    background-color: var(--lumo-success-color-10pct);
  }

  .bg-warning {
    background-color: var(--lumo-warning-color);
  }
  .bg-warning-10 {
    background-color: var(--lumo-warning-color-10pct);
  }
`;/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Go=y`
  /* === Border === */
  .border-0 {
    border: none;
  }
  .border {
    border: 1px solid;
  }
  .border-b {
    border-bottom: 1px solid;
  }
  .border-l {
    border-left: 1px solid;
  }
  .border-r {
    border-right: 1px solid;
  }
  .border-t {
    border-top: 1px solid;
  }

  /* === Border color === */
  .border-contrast-5 {
    border-color: var(--lumo-contrast-5pct);
  }
  .border-contrast-10 {
    border-color: var(--lumo-contrast-10pct);
  }
  .border-contrast-20 {
    border-color: var(--lumo-contrast-20pct);
  }
  .border-contrast-30 {
    border-color: var(--lumo-contrast-30pct);
  }
  .border-contrast-40 {
    border-color: var(--lumo-contrast-40pct);
  }
  .border-contrast-50 {
    border-color: var(--lumo-contrast-50pct);
  }
  .border-contrast-60 {
    border-color: var(--lumo-contrast-60pct);
  }
  .border-contrast-70 {
    border-color: var(--lumo-contrast-70pct);
  }
  .border-contrast-80 {
    border-color: var(--lumo-contrast-80pct);
  }
  .border-contrast-90 {
    border-color: var(--lumo-contrast-90pct);
  }
  .border-contrast {
    border-color: var(--lumo-contrast);
  }

  .border-primary {
    border-color: var(--lumo-primary-color);
  }
  .border-primary-50 {
    border-color: var(--lumo-primary-color-50pct);
  }
  .border-primary-10 {
    border-color: var(--lumo-primary-color-10pct);
  }

  .border-error {
    border-color: var(--lumo-error-color);
  }
  .border-error-50 {
    border-color: var(--lumo-error-color-50pct);
  }
  .border-error-10 {
    border-color: var(--lumo-error-color-10pct);
  }

  .border-success {
    border-color: var(--lumo-success-color);
  }
  .border-success-50 {
    border-color: var(--lumo-success-color-50pct);
  }
  .border-success-10 {
    border-color: var(--lumo-success-color-10pct);
  }

  .border-warning {
    border-color: var(--lumo-warning-color);
  }
  .border-warning-10 {
    border-color: var(--lumo-warning-color-10pct);
  }
  .border-warning-strong {
    border-color: var(--lumo-warning-text-color);
  }

  /* === Border radius === */
  .rounded-none {
    border-radius: 0;
  }
  .rounded-s {
    border-radius: var(--lumo-border-radius-s);
  }
  .rounded-m {
    border-radius: var(--lumo-border-radius-m);
  }
  .rounded-l {
    border-radius: var(--lumo-border-radius-l);
  }
`;/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Ko=y`
  /* === Align content === */
  .content-center {
    align-content: center;
  }
  .content-end {
    align-content: flex-end;
  }
  .content-start {
    align-content: flex-start;
  }
  .content-around {
    align-content: space-around;
  }
  .content-between {
    align-content: space-between;
  }
  .content-evenly {
    align-content: space-evenly;
  }
  .content-stretch {
    align-content: stretch;
  }

  /* === Align items === */
  .items-baseline {
    align-items: baseline;
  }
  .items-center {
    align-items: center;
  }
  .items-end {
    align-items: flex-end;
  }
  .items-start {
    align-items: flex-start;
  }
  .items-stretch {
    align-items: stretch;
  }

  /* === Align self === */
  .self-auto {
    align-self: auto;
  }
  .self-baseline {
    align-self: baseline;
  }
  .self-center {
    align-self: center;
  }
  .self-end {
    align-self: flex-end;
  }
  .self-start {
    align-self: flex-start;
  }
  .self-stretch {
    align-self: stretch;
  }

  /* === Flex === */
  .flex-auto {
    flex: auto;
  }
  .flex-none {
    flex: none;
  }

  /* === Flex direction === */
  .flex-col {
    flex-direction: column;
  }
  .flex-col-reverse {
    flex-direction: column-reverse;
  }
  .flex-row {
    flex-direction: row;
  }
  .flex-row-reverse {
    flex-direction: row-reverse;
  }

  /* === Flex grow === */
  .flex-grow-0 {
    flex-grow: 0;
  }
  .flex-grow {
    flex-grow: 1;
  }

  /* === Flex shrink === */
  .flex-shrink-0 {
    flex-shrink: 0;
  }
  .flex-shrink {
    flex-shrink: 1;
  }

  /* === Flex wrap === */
  .flex-nowrap {
    flex-wrap: nowrap;
  }
  .flex-wrap {
    flex-wrap: wrap;
  }
  .flex-wrap-reverse {
    flex-wrap: wrap-reverse;
  }

  /* === Gap === */
  .gap-xs {
    gap: var(--lumo-space-xs);
  }
  .gap-s {
    gap: var(--lumo-space-s);
  }
  .gap-m {
    gap: var(--lumo-space-m);
  }
  .gap-l {
    gap: var(--lumo-space-l);
  }
  .gap-xl {
    gap: var(--lumo-space-xl);
  }

  /* === Gap (column) === */
  .gap-x-xs {
    column-gap: var(--lumo-space-xs);
  }
  .gap-x-s {
    column-gap: var(--lumo-space-s);
  }
  .gap-x-m {
    column-gap: var(--lumo-space-m);
  }
  .gap-x-l {
    column-gap: var(--lumo-space-l);
  }
  .gap-x-xl {
    column-gap: var(--lumo-space-xl);
  }

  /* === Gap (row) === */
  .gap-y-xs {
    row-gap: var(--lumo-space-xs);
  }
  .gap-y-s {
    row-gap: var(--lumo-space-s);
  }
  .gap-y-m {
    row-gap: var(--lumo-space-m);
  }
  .gap-y-l {
    row-gap: var(--lumo-space-l);
  }
  .gap-y-xl {
    row-gap: var(--lumo-space-xl);
  }

  /* === Grid auto flow === */
  .grid-flow-col {
    grid-auto-flow: column;
  }
  .grid-flow-row {
    grid-auto-flow: row;
  }

  /* === Grid columns === */
  .grid-cols-1 {
    grid-template-columns: repeat(1, minmax(0, 1fr));
  }
  .grid-cols-2 {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
  .grid-cols-3 {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
  .grid-cols-4 {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }
  .grid-cols-5 {
    grid-template-columns: repeat(5, minmax(0, 1fr));
  }
  .grid-cols-6 {
    grid-template-columns: repeat(6, minmax(0, 1fr));
  }
  .grid-cols-7 {
    grid-template-columns: repeat(7, minmax(0, 1fr));
  }
  .grid-cols-8 {
    grid-template-columns: repeat(8, minmax(0, 1fr));
  }
  .grid-cols-9 {
    grid-template-columns: repeat(9, minmax(0, 1fr));
  }
  .grid-cols-10 {
    grid-template-columns: repeat(10, minmax(0, 1fr));
  }
  .grid-cols-11 {
    grid-template-columns: repeat(11, minmax(0, 1fr));
  }
  .grid-cols-12 {
    grid-template-columns: repeat(12, minmax(0, 1fr));
  }

  /* === Grid rows === */
  .grid-rows-1 {
    grid-template-rows: repeat(1, minmax(0, 1fr));
  }
  .grid-rows-2 {
    grid-template-rows: repeat(2, minmax(0, 1fr));
  }
  .grid-rows-3 {
    grid-template-rows: repeat(3, minmax(0, 1fr));
  }
  .grid-rows-4 {
    grid-template-rows: repeat(4, minmax(0, 1fr));
  }
  .grid-rows-5 {
    grid-template-rows: repeat(5, minmax(0, 1fr));
  }
  .grid-rows-6 {
    grid-template-rows: repeat(6, minmax(0, 1fr));
  }

  /* === Justify content === */
  .justify-center {
    justify-content: center;
  }
  .justify-end {
    justify-content: flex-end;
  }
  .justify-start {
    justify-content: flex-start;
  }
  .justify-around {
    justify-content: space-around;
  }
  .justify-between {
    justify-content: space-between;
  }
  .justify-evenly {
    justify-content: space-evenly;
  }

  /* === Span (column) === */
  .col-span-1 {
    grid-column: span 1 / span 1;
  }
  .col-span-2 {
    grid-column: span 2 / span 2;
  }
  .col-span-3 {
    grid-column: span 3 / span 3;
  }
  .col-span-4 {
    grid-column: span 4 / span 4;
  }
  .col-span-5 {
    grid-column: span 5 / span 5;
  }
  .col-span-6 {
    grid-column: span 6 / span 6;
  }
  .col-span-7 {
    grid-column: span 7 / span 7;
  }
  .col-span-8 {
    grid-column: span 8 / span 8;
  }
  .col-span-9 {
    grid-column: span 9 / span 9;
  }
  .col-span-10 {
    grid-column: span 10 / span 10;
  }
  .col-span-11 {
    grid-column: span 11 / span 11;
  }
  .col-span-12 {
    grid-column: span 12 / span 12;
  }

  /* === Span (row) === */
  .row-span-1 {
    grid-row: span 1 / span 1;
  }
  .row-span-2 {
    grid-row: span 2 / span 2;
  }
  .row-span-3 {
    grid-row: span 3 / span 3;
  }
  .row-span-4 {
    grid-row: span 4 / span 4;
  }
  .row-span-5 {
    grid-row: span 5 / span 5;
  }
  .row-span-6 {
    grid-row: span 6 / span 6;
  }

  /* === Responsive design === */
  @media (min-width: 640px) {
    .sm\\:flex-col {
      flex-direction: column;
    }
    .sm\\:flex-row {
      flex-direction: row;
    }
    .sm\\:grid-cols-1 {
      grid-template-columns: repeat(1, minmax(0, 1fr));
    }
    .sm\\:grid-cols-2 {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }
    .sm\\:grid-cols-3 {
      grid-template-columns: repeat(3, minmax(0, 1fr));
    }
    .sm\\:grid-cols-4 {
      grid-template-columns: repeat(4, minmax(0, 1fr));
    }
    .sm\\:grid-cols-5 {
      grid-template-columns: repeat(5, minmax(0, 1fr));
    }
    .sm\\:grid-cols-6 {
      grid-template-columns: repeat(6, minmax(0, 1fr));
    }
    .sm\\:grid-cols-7 {
      grid-template-columns: repeat(7, minmax(0, 1fr));
    }
    .sm\\:grid-cols-8 {
      grid-template-columns: repeat(8, minmax(0, 1fr));
    }
    .sm\\:grid-cols-9 {
      grid-template-columns: repeat(9, minmax(0, 1fr));
    }
    .sm\\:grid-cols-10 {
      grid-template-columns: repeat(10, minmax(0, 1fr));
    }
    .sm\\:grid-cols-11 {
      grid-template-columns: repeat(11, minmax(0, 1fr));
    }
    .sm\\:grid-cols-12 {
      grid-template-columns: repeat(12, minmax(0, 1fr));
    }
  }

  @media (min-width: 768px) {
    .md\\:flex-col {
      flex-direction: column;
    }
    .md\\:flex-row {
      flex-direction: row;
    }
    .md\\:grid-cols-1 {
      grid-template-columns: repeat(1, minmax(0, 1fr));
    }
    .md\\:grid-cols-2 {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }
    .md\\:grid-cols-3 {
      grid-template-columns: repeat(3, minmax(0, 1fr));
    }
    .md\\:grid-cols-4 {
      grid-template-columns: repeat(4, minmax(0, 1fr));
    }
    .md\\:grid-cols-5 {
      grid-template-columns: repeat(5, minmax(0, 1fr));
    }
    .md\\:grid-cols-6 {
      grid-template-columns: repeat(6, minmax(0, 1fr));
    }
    .md\\:grid-cols-7 {
      grid-template-columns: repeat(7, minmax(0, 1fr));
    }
    .md\\:grid-cols-8 {
      grid-template-columns: repeat(8, minmax(0, 1fr));
    }
    .md\\:grid-cols-9 {
      grid-template-columns: repeat(9, minmax(0, 1fr));
    }
    .md\\:grid-cols-10 {
      grid-template-columns: repeat(10, minmax(0, 1fr));
    }
    .md\\:grid-cols-11 {
      grid-template-columns: repeat(11, minmax(0, 1fr));
    }
    .md\\:grid-cols-12 {
      grid-template-columns: repeat(12, minmax(0, 1fr));
    }
  }
  @media (min-width: 1024px) {
    .lg\\:flex-col {
      flex-direction: column;
    }
    .lg\\:flex-row {
      flex-direction: row;
    }
    .lg\\:grid-cols-1 {
      grid-template-columns: repeat(1, minmax(0, 1fr));
    }
    .lg\\:grid-cols-2 {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }
    .lg\\:grid-cols-3 {
      grid-template-columns: repeat(3, minmax(0, 1fr));
    }
    .lg\\:grid-cols-4 {
      grid-template-columns: repeat(4, minmax(0, 1fr));
    }
    .lg\\:grid-cols-5 {
      grid-template-columns: repeat(5, minmax(0, 1fr));
    }
    .lg\\:grid-cols-6 {
      grid-template-columns: repeat(6, minmax(0, 1fr));
    }
    .lg\\:grid-cols-7 {
      grid-template-columns: repeat(7, minmax(0, 1fr));
    }
    .lg\\:grid-cols-8 {
      grid-template-columns: repeat(8, minmax(0, 1fr));
    }
    .lg\\:grid-cols-9 {
      grid-template-columns: repeat(9, minmax(0, 1fr));
    }
    .lg\\:grid-cols-10 {
      grid-template-columns: repeat(10, minmax(0, 1fr));
    }
    .lg\\:grid-cols-11 {
      grid-template-columns: repeat(11, minmax(0, 1fr));
    }
    .lg\\:grid-cols-12 {
      grid-template-columns: repeat(12, minmax(0, 1fr));
    }
  }
  @media (min-width: 1280px) {
    .xl\\:flex-col {
      flex-direction: column;
    }
    .xl\\:flex-row {
      flex-direction: row;
    }
    .xl\\:grid-cols-1 {
      grid-template-columns: repeat(1, minmax(0, 1fr));
    }
    .xl\\:grid-cols-2 {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }
    .xl\\:grid-cols-3 {
      grid-template-columns: repeat(3, minmax(0, 1fr));
    }
    .xl\\:grid-cols-4 {
      grid-template-columns: repeat(4, minmax(0, 1fr));
    }
    .xl\\:grid-cols-5 {
      grid-template-columns: repeat(5, minmax(0, 1fr));
    }
    .xl\\:grid-cols-6 {
      grid-template-columns: repeat(6, minmax(0, 1fr));
    }
    .xl\\:grid-cols-7 {
      grid-template-columns: repeat(7, minmax(0, 1fr));
    }
    .xl\\:grid-cols-8 {
      grid-template-columns: repeat(8, minmax(0, 1fr));
    }
    .xl\\:grid-cols-9 {
      grid-template-columns: repeat(9, minmax(0, 1fr));
    }
    .xl\\:grid-cols-10 {
      grid-template-columns: repeat(10, minmax(0, 1fr));
    }
    .xl\\:grid-cols-11 {
      grid-template-columns: repeat(11, minmax(0, 1fr));
    }
    .xl\\:grid-cols-12 {
      grid-template-columns: repeat(12, minmax(0, 1fr));
    }
  }
  @media (min-width: 1536px) {
    .\\32xl\\:flex-col {
      flex-direction: column;
    }
    .\\32xl\\:flex-row {
      flex-direction: row;
    }
    .\\32xl\\:grid-cols-1 {
      grid-template-columns: repeat(1, minmax(0, 1fr));
    }
    .\\32xl\\:grid-cols-2 {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }
    .\\32xl\\:grid-cols-3 {
      grid-template-columns: repeat(3, minmax(0, 1fr));
    }
    .\\32xl\\:grid-cols-4 {
      grid-template-columns: repeat(4, minmax(0, 1fr));
    }
    .\\32xl\\:grid-cols-5 {
      grid-template-columns: repeat(5, minmax(0, 1fr));
    }
    .\\32xl\\:grid-cols-6 {
      grid-template-columns: repeat(6, minmax(0, 1fr));
    }
    .\\32xl\\:grid-cols-7 {
      grid-template-columns: repeat(7, minmax(0, 1fr));
    }
    .\\32xl\\:grid-cols-8 {
      grid-template-columns: repeat(8, minmax(0, 1fr));
    }
    .\\32xl\\:grid-cols-9 {
      grid-template-columns: repeat(9, minmax(0, 1fr));
    }
    .\\32xl\\:grid-cols-10 {
      grid-template-columns: repeat(10, minmax(0, 1fr));
    }
    .\\32xl\\:grid-cols-11 {
      grid-template-columns: repeat(11, minmax(0, 1fr));
    }
    .\\32xl\\:grid-cols-12 {
      grid-template-columns: repeat(12, minmax(0, 1fr));
    }
  }
`;/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Qo=y`
  /* === Box sizing === */
  .box-border {
    box-sizing: border-box;
  }
  .box-content {
    box-sizing: content-box;
  }

  /* === Display === */
  .block {
    display: block;
  }
  .flex {
    display: flex;
  }
  .hidden {
    display: none;
  }
  .inline {
    display: inline;
  }
  .inline-block {
    display: inline-block;
  }
  .inline-flex {
    display: inline-flex;
  }
  .inline-grid {
    display: inline-grid;
  }
  .grid {
    display: grid;
  }

  /* === Overflow === */
  .overflow-auto {
    overflow: auto;
  }
  .overflow-hidden {
    overflow: hidden;
  }
  .overflow-scroll {
    overflow: scroll;
  }

  /* === Position === */
  .absolute {
    position: absolute;
  }
  .fixed {
    position: fixed;
  }
  .static {
    position: static;
  }
  .sticky {
    position: sticky;
  }
  .relative {
    position: relative;
  }

  /* === Responsive design === */
  @media (min-width: 640px) {
    .sm\\:flex {
      display: flex;
    }
    .sm\\:hidden {
      display: none;
    }
  }
  @media (min-width: 768px) {
    .md\\:flex {
      display: flex;
    }
    .md\\:hidden {
      display: none;
    }
  }
  @media (min-width: 1024px) {
    .lg\\:flex {
      display: flex;
    }
    .lg\\:hidden {
      display: none;
    }
  }
  @media (min-width: 1280px) {
    .xl\\:flex {
      display: flex;
    }
    .xl\\:hidden {
      display: none;
    }
  }
  @media (min-width: 1536px) {
    .\\32xl\\:flex {
      display: flex;
    }
    .\\32xl\\:hidden {
      display: none;
    }
  }
`;/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Jo=y`
  /* === Box shadows === */
  .shadow-xs {
    box-shadow: var(--lumo-box-shadow-xs);
  }
  .shadow-s {
    box-shadow: var(--lumo-box-shadow-s);
  }
  .shadow-m {
    box-shadow: var(--lumo-box-shadow-m);
  }
  .shadow-l {
    box-shadow: var(--lumo-box-shadow-l);
  }
  .shadow-xl {
    box-shadow: var(--lumo-box-shadow-xl);
  }
`;/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Xo=y`
  /* === Height === */
  .h-0 {
    height: 0;
  }
  .h-xs {
    height: var(--lumo-size-xs);
  }
  .h-s {
    height: var(--lumo-size-s);
  }
  .h-m {
    height: var(--lumo-size-m);
  }
  .h-l {
    height: var(--lumo-size-l);
  }
  .h-xl {
    height: var(--lumo-size-xl);
  }
  .h-auto {
    height: auto;
  }
  .h-full {
    height: 100%;
  }
  .h-screen {
    height: 100vh;
  }

  /* === Height (max) === */
  .max-h-full {
    max-height: 100%;
  }
  .max-h-screen {
    max-height: 100vh;
  }

  /* === Height (min) === */
  .min-h-0 {
    min-height: 0;
  }
  .min-h-full {
    min-height: 100%;
  }
  .min-h-screen {
    min-height: 100vh;
  }

  /* === Icon sizing === */
  .icon-s {
    height: var(--lumo-icon-size-s);
    width: var(--lumo-icon-size-s);
  }
  .icon-m {
    height: var(--lumo-icon-size-m);
    width: var(--lumo-icon-size-m);
  }
  .icon-l {
    height: var(--lumo-icon-size-l);
    width: var(--lumo-icon-size-l);
  }

  /* === Width === */
  .w-xs {
    width: var(--lumo-size-xs);
  }
  .w-s {
    width: var(--lumo-size-s);
  }
  .w-m {
    width: var(--lumo-size-m);
  }
  .w-l {
    width: var(--lumo-size-l);
  }
  .w-xl {
    width: var(--lumo-size-xl);
  }
  .w-auto {
    width: auto;
  }
  .w-full {
    width: 100%;
  }

  /* === Width (max) === */
  .max-w-full {
    max-width: 100%;
  }
  .max-w-screen-sm {
    max-width: 640px;
  }
  .max-w-screen-md {
    max-width: 768px;
  }
  .max-w-screen-lg {
    max-width: 1024px;
  }
  .max-w-screen-xl {
    max-width: 1280px;
  }
  .max-w-screen-2xl {
    max-width: 1536px;
  }

  /* === Width (min) === */
  .min-w-0 {
    min-width: 0;
  }
  .min-w-full {
    min-width: 100%;
  }
`;/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Yo=y`
  /* === Margin === */
  .m-auto {
    margin: auto;
  }
  .m-0 {
    margin: 0;
  }
  .m-xs {
    margin: var(--lumo-space-xs);
  }
  .m-s {
    margin: var(--lumo-space-s);
  }
  .m-m {
    margin: var(--lumo-space-m);
  }
  .m-l {
    margin: var(--lumo-space-l);
  }
  .m-xl {
    margin: var(--lumo-space-xl);
  }

  /* === Margin (bottom) === */
  .mb-auto {
    margin-bottom: auto;
  }
  .mb-0 {
    margin-bottom: 0;
  }
  .mb-xs {
    margin-bottom: var(--lumo-space-xs);
  }
  .mb-s {
    margin-bottom: var(--lumo-space-s);
  }
  .mb-m {
    margin-bottom: var(--lumo-space-m);
  }
  .mb-l {
    margin-bottom: var(--lumo-space-l);
  }
  .mb-xl {
    margin-bottom: var(--lumo-space-xl);
  }

  /* === Margin (end) === */
  .me-auto {
    margin-inline-end: auto;
  }
  .me-0 {
    margin-inline-end: 0;
  }
  .me-xs {
    margin-inline-end: var(--lumo-space-xs);
  }
  .me-s {
    margin-inline-end: var(--lumo-space-s);
  }
  .me-m {
    margin-inline-end: var(--lumo-space-m);
  }
  .me-l {
    margin-inline-end: var(--lumo-space-l);
  }
  .me-xl {
    margin-inline-end: var(--lumo-space-xl);
  }

  /* === Margin (horizontal) === */
  .mx-auto {
    margin-left: auto;
    margin-right: auto;
  }
  .mx-0 {
    margin-left: 0;
    margin-right: 0;
  }
  .mx-xs {
    margin-left: var(--lumo-space-xs);
    margin-right: var(--lumo-space-xs);
  }
  .mx-s {
    margin-left: var(--lumo-space-s);
    margin-right: var(--lumo-space-s);
  }
  .mx-m {
    margin-left: var(--lumo-space-m);
    margin-right: var(--lumo-space-m);
  }
  .mx-l {
    margin-left: var(--lumo-space-l);
    margin-right: var(--lumo-space-l);
  }
  .mx-xl {
    margin-left: var(--lumo-space-xl);
    margin-right: var(--lumo-space-xl);
  }

  /* === Margin (left) === */
  .ml-auto {
    margin-left: auto;
  }
  .ml-0 {
    margin-left: 0;
  }
  .ml-xs {
    margin-left: var(--lumo-space-xs);
  }
  .ml-s {
    margin-left: var(--lumo-space-s);
  }
  .ml-m {
    margin-left: var(--lumo-space-m);
  }
  .ml-l {
    margin-left: var(--lumo-space-l);
  }
  .ml-xl {
    margin-left: var(--lumo-space-xl);
  }

  /* === Margin (right) === */
  .mr-auto {
    margin-right: auto;
  }
  .mr-0 {
    margin-right: 0;
  }
  .mr-xs {
    margin-right: var(--lumo-space-xs);
  }
  .mr-s {
    margin-right: var(--lumo-space-s);
  }
  .mr-m {
    margin-right: var(--lumo-space-m);
  }
  .mr-l {
    margin-right: var(--lumo-space-l);
  }
  .mr-xl {
    margin-right: var(--lumo-space-xl);
  }

  /* === Margin (start) === */
  .ms-auto {
    margin-inline-start: auto;
  }
  .ms-0 {
    margin-inline-start: 0;
  }
  .ms-xs {
    margin-inline-start: var(--lumo-space-xs);
  }
  .ms-s {
    margin-inline-start: var(--lumo-space-s);
  }
  .ms-m {
    margin-inline-start: var(--lumo-space-m);
  }
  .ms-l {
    margin-inline-start: var(--lumo-space-l);
  }
  .ms-xl {
    margin-inline-start: var(--lumo-space-xl);
  }

  /* === Margin (top) === */
  .mt-auto {
    margin-top: auto;
  }
  .mt-0 {
    margin-top: 0;
  }
  .mt-xs {
    margin-top: var(--lumo-space-xs);
  }
  .mt-s {
    margin-top: var(--lumo-space-s);
  }
  .mt-m {
    margin-top: var(--lumo-space-m);
  }
  .mt-l {
    margin-top: var(--lumo-space-l);
  }
  .mt-xl {
    margin-top: var(--lumo-space-xl);
  }

  /* === Margin (vertical) === */
  .my-auto {
    margin-bottom: auto;
    margin-top: auto;
  }
  .my-0 {
    margin-bottom: 0;
    margin-top: 0;
  }
  .my-xs {
    margin-bottom: var(--lumo-space-xs);
    margin-top: var(--lumo-space-xs);
  }
  .my-s {
    margin-bottom: var(--lumo-space-s);
    margin-top: var(--lumo-space-s);
  }
  .my-m {
    margin-bottom: var(--lumo-space-m);
    margin-top: var(--lumo-space-m);
  }
  .my-l {
    margin-bottom: var(--lumo-space-l);
    margin-top: var(--lumo-space-l);
  }
  .my-xl {
    margin-bottom: var(--lumo-space-xl);
    margin-top: var(--lumo-space-xl);
  }

  /* === Padding === */
  .p-0 {
    padding: 0;
  }
  .p-xs {
    padding: var(--lumo-space-xs);
  }
  .p-s {
    padding: var(--lumo-space-s);
  }
  .p-m {
    padding: var(--lumo-space-m);
  }
  .p-l {
    padding: var(--lumo-space-l);
  }
  .p-xl {
    padding: var(--lumo-space-xl);
  }

  /* === Padding (bottom) === */
  .pb-0 {
    padding-bottom: 0;
  }
  .pb-xs {
    padding-bottom: var(--lumo-space-xs);
  }
  .pb-s {
    padding-bottom: var(--lumo-space-s);
  }
  .pb-m {
    padding-bottom: var(--lumo-space-m);
  }
  .pb-l {
    padding-bottom: var(--lumo-space-l);
  }
  .pb-xl {
    padding-bottom: var(--lumo-space-xl);
  }

  /* === Padding (end) === */
  .pe-0 {
    padding-inline-end: 0;
  }
  .pe-xs {
    padding-inline-end: var(--lumo-space-xs);
  }
  .pe-s {
    padding-inline-end: var(--lumo-space-s);
  }
  .pe-m {
    padding-inline-end: var(--lumo-space-m);
  }
  .pe-l {
    padding-inline-end: var(--lumo-space-l);
  }
  .pe-xl {
    padding-inline-end: var(--lumo-space-xl);
  }

  /* === Padding (horizontal) === */
  .px-0 {
    padding-left: 0;
    padding-right: 0;
  }
  .px-xs {
    padding-left: var(--lumo-space-xs);
    padding-right: var(--lumo-space-xs);
  }
  .px-s {
    padding-left: var(--lumo-space-s);
    padding-right: var(--lumo-space-s);
  }
  .px-m {
    padding-left: var(--lumo-space-m);
    padding-right: var(--lumo-space-m);
  }
  .px-l {
    padding-left: var(--lumo-space-l);
    padding-right: var(--lumo-space-l);
  }
  .px-xl {
    padding-left: var(--lumo-space-xl);
    padding-right: var(--lumo-space-xl);
  }

  /* === Padding (left) === */
  .pl-0 {
    padding-left: 0;
  }
  .pl-xs {
    padding-left: var(--lumo-space-xs);
  }
  .pl-s {
    padding-left: var(--lumo-space-s);
  }
  .pl-m {
    padding-left: var(--lumo-space-m);
  }
  .pl-l {
    padding-left: var(--lumo-space-l);
  }
  .pl-xl {
    padding-left: var(--lumo-space-xl);
  }

  /* === Padding (right) === */
  .pr-0 {
    padding-right: 0;
  }
  .pr-xs {
    padding-right: var(--lumo-space-xs);
  }
  .pr-s {
    padding-right: var(--lumo-space-s);
  }
  .pr-m {
    padding-right: var(--lumo-space-m);
  }
  .pr-l {
    padding-right: var(--lumo-space-l);
  }
  .pr-xl {
    padding-right: var(--lumo-space-xl);
  }

  /* === Padding (start) === */
  .ps-0 {
    padding-inline-start: 0;
  }
  .ps-xs {
    padding-inline-start: var(--lumo-space-xs);
  }
  .ps-s {
    padding-inline-start: var(--lumo-space-s);
  }
  .ps-m {
    padding-inline-start: var(--lumo-space-m);
  }
  .ps-l {
    padding-inline-start: var(--lumo-space-l);
  }
  .ps-xl {
    padding-inline-start: var(--lumo-space-xl);
  }

  /* === Padding (top) === */
  .pt-0 {
    padding-top: 0;
  }
  .pt-xs {
    padding-top: var(--lumo-space-xs);
  }
  .pt-s {
    padding-top: var(--lumo-space-s);
  }
  .pt-m {
    padding-top: var(--lumo-space-m);
  }
  .pt-l {
    padding-top: var(--lumo-space-l);
  }
  .pt-xl {
    padding-top: var(--lumo-space-xl);
  }

  /* === Padding (vertical) === */
  .py-0 {
    padding-bottom: 0;
    padding-top: 0;
  }
  .py-xs {
    padding-bottom: var(--lumo-space-xs);
    padding-top: var(--lumo-space-xs);
  }
  .py-s {
    padding-bottom: var(--lumo-space-s);
    padding-top: var(--lumo-space-s);
  }
  .py-m {
    padding-bottom: var(--lumo-space-m);
    padding-top: var(--lumo-space-m);
  }
  .py-l {
    padding-bottom: var(--lumo-space-l);
    padding-top: var(--lumo-space-l);
  }
  .py-xl {
    padding-bottom: var(--lumo-space-xl);
    padding-top: var(--lumo-space-xl);
  }
`;/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Zo=y`
  /* === Font size === */
  .text-2xs {
    font-size: var(--lumo-font-size-xxs);
  }
  .text-xs {
    font-size: var(--lumo-font-size-xs);
  }
  .text-s {
    font-size: var(--lumo-font-size-s);
  }
  .text-m {
    font-size: var(--lumo-font-size-m);
  }
  .text-l {
    font-size: var(--lumo-font-size-l);
  }
  .text-xl {
    font-size: var(--lumo-font-size-xl);
  }
  .text-2xl {
    font-size: var(--lumo-font-size-xxl);
  }
  .text-3xl {
    font-size: var(--lumo-font-size-xxxl);
  }

  /* === Font weight === */
  .font-thin {
    font-weight: 100;
  }
  .font-extralight {
    font-weight: 200;
  }
  .font-light {
    font-weight: 300;
  }
  .font-normal {
    font-weight: 400;
  }
  .font-medium {
    font-weight: 500;
  }
  .font-semibold {
    font-weight: 600;
  }
  .font-bold {
    font-weight: 700;
  }
  .font-extrabold {
    font-weight: 800;
  }
  .font-black {
    font-weight: 900;
  }

  /* === Line height === */
  .leading-none {
    line-height: 1;
  }
  .leading-xs {
    line-height: var(--lumo-line-height-xs);
  }
  .leading-s {
    line-height: var(--lumo-line-height-s);
  }
  .leading-m {
    line-height: var(--lumo-line-height-m);
  }

  /* === List style type === */
  .list-none {
    list-style-type: none;
  }

  /* === Text alignment === */
  .text-left {
    text-align: left;
  }
  .text-center {
    text-align: center;
  }
  .text-right {
    text-align: right;
  }
  .text-justify {
    text-align: justify;
  }

  /* === Text color === */
  .text-header {
    color: var(--lumo-header-text-color);
  }
  .text-body {
    color: var(--lumo-body-text-color);
  }
  .text-secondary {
    color: var(--lumo-secondary-text-color);
  }
  .text-tertiary {
    color: var(--lumo-tertiary-text-color);
  }
  .text-disabled {
    color: var(--lumo-disabled-text-color);
  }
  .text-primary {
    color: var(--lumo-primary-text-color);
  }
  .text-primary-contrast {
    color: var(--lumo-primary-contrast-color);
  }
  .text-error {
    color: var(--lumo-error-text-color);
  }
  .text-error-contrast {
    color: var(--lumo-error-contrast-color);
  }
  .text-success {
    color: var(--lumo-success-text-color);
  }
  .text-success-contrast {
    color: var(--lumo-success-contrast-color);
  }
  .text-warning {
    color: var(--lumo-warning-text-color);
  }
  .text-warning-contrast {
    color: var(--lumo-warning-contrast-color);
  }

  /* === Text overflow === */
  .overflow-clip {
    text-overflow: clip;
  }
  .overflow-ellipsis {
    text-overflow: ellipsis;
  }

  /* === Text transform === */
  .capitalize {
    text-transform: capitalize;
  }
  .lowercase {
    text-transform: lowercase;
  }
  .uppercase {
    text-transform: uppercase;
  }

  /* === Whitespace === */
  .whitespace-normal {
    white-space: normal;
  }
  .whitespace-nowrap {
    white-space: nowrap;
  }
  .whitespace-pre {
    white-space: pre;
  }
  .whitespace-pre-line {
    white-space: pre-line;
  }
  .whitespace-pre-wrap {
    white-space: pre-wrap;
  }

  /* === Responsive design === */
  @media (min-width: 640px) {
    .sm\\:text-2xs {
      font-size: var(--lumo-font-size-xxs);
    }
    .sm\\:text-xs {
      font-size: var(--lumo-font-size-xs);
    }
    .sm\\:text-s {
      font-size: var(--lumo-font-size-s);
    }
    .sm\\:text-m {
      font-size: var(--lumo-font-size-m);
    }
    .sm\\:text-l {
      font-size: var(--lumo-font-size-l);
    }
    .sm\\:text-xl {
      font-size: var(--lumo-font-size-xl);
    }
    .sm\\:text-2xl {
      font-size: var(--lumo-font-size-xxl);
    }
    .sm\\:text-3xl {
      font-size: var(--lumo-font-size-xxxl);
    }
  }
  @media (min-width: 768px) {
    .md\\:text-2xs {
      font-size: var(--lumo-font-size-xxs);
    }
    .md\\:text-xs {
      font-size: var(--lumo-font-size-xs);
    }
    .md\\:text-s {
      font-size: var(--lumo-font-size-s);
    }
    .md\\:text-m {
      font-size: var(--lumo-font-size-m);
    }
    .md\\:text-l {
      font-size: var(--lumo-font-size-l);
    }
    .md\\:text-xl {
      font-size: var(--lumo-font-size-xl);
    }
    .md\\:text-2xl {
      font-size: var(--lumo-font-size-xxl);
    }
    .md\\:text-3xl {
      font-size: var(--lumo-font-size-xxxl);
    }
  }
  @media (min-width: 1024px) {
    .lg\\:text-2xs {
      font-size: var(--lumo-font-size-xxs);
    }
    .lg\\:text-xs {
      font-size: var(--lumo-font-size-xs);
    }
    .lg\\:text-s {
      font-size: var(--lumo-font-size-s);
    }
    .lg\\:text-m {
      font-size: var(--lumo-font-size-m);
    }
    .lg\\:text-l {
      font-size: var(--lumo-font-size-l);
    }
    .lg\\:text-xl {
      font-size: var(--lumo-font-size-xl);
    }
    .lg\\:text-2xl {
      font-size: var(--lumo-font-size-xxl);
    }
    .lg\\:text-3xl {
      font-size: var(--lumo-font-size-xxxl);
    }
  }
  @media (min-width: 1280px) {
    .xl\\:text-2xs {
      font-size: var(--lumo-font-size-xxs);
    }
    .xl\\:text-xs {
      font-size: var(--lumo-font-size-xs);
    }
    .xl\\:text-s {
      font-size: var(--lumo-font-size-s);
    }
    .xl\\:text-m {
      font-size: var(--lumo-font-size-m);
    }
    .xl\\:text-l {
      font-size: var(--lumo-font-size-l);
    }
    .xl\\:text-xl {
      font-size: var(--lumo-font-size-xl);
    }
    .xl\\:text-2xl {
      font-size: var(--lumo-font-size-xxl);
    }
    .xl\\:text-3xl {
      font-size: var(--lumo-font-size-xxxl);
    }
  }
  @media (min-width: 1536px) {
    .\\32xl\\:text-2xs {
      font-size: var(--lumo-font-size-xxs);
    }
    .\\32xl\\:text-xs {
      font-size: var(--lumo-font-size-xs);
    }
    .\\32xl\\:text-s {
      font-size: var(--lumo-font-size-s);
    }
    .\\32xl\\:text-m {
      font-size: var(--lumo-font-size-m);
    }
    .\\32xl\\:text-l {
      font-size: var(--lumo-font-size-l);
    }
    .\\32xl\\:text-xl {
      font-size: var(--lumo-font-size-xl);
    }
    .\\32xl\\:text-2xl {
      font-size: var(--lumo-font-size-xxl);
    }
    .\\32xl\\:text-3xl {
      font-size: var(--lumo-font-size-xxxl);
    }
  }
`;/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Lt=y`
${Ho}
${qo}
${Go}
${Jo}
${Ko}
${Qo}
${Xo}
${Yo}
${Zo}
`;_e("",Lt,{moduleId:"lumo-utility"});/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */q("utility",Lt);const ei=r=>{const e=[];r!==document&&(e.push(me(kt.cssText,"",r,!0)),e.push(me(Ot.cssText,"",r,!0)),e.push(me(Ur.cssText,"",r,!0)),e.push(me(At.cssText,"",r,!0)),e.push(me(Lt.cssText,"",r,!0)))},ti=ei;ti(document);export{ho as D,ce as L,mo as P,ii as T,gt as _,Ot as a,po as b,y as c,Do as d,Vr as e,q as f,b as g,eo as h,me as i,Ro as j,Oe as k,X as n,_e as r,ni as s,kt as t,Pt as u};
