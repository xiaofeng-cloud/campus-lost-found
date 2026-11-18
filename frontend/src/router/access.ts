export interface RouteAccessMeta { requiresAuth?: boolean; requiresAdmin?: boolean }
export interface AccessUser { role: string }

export function decideAccess(meta: RouteAccessMeta, user: AccessUser | null, fullPath: string) {
  if (meta.requiresAuth && !user) return { name: 'login', query: { redirect: fullPath } }
  if (meta.requiresAdmin && user?.role !== 'ADMIN') return { name: 'home' }
  return true
}
